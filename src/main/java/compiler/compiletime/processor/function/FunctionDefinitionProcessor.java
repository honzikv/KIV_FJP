package compiler.compiletime.processor.function;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
import compiler.compiletime.utils.VariableUtils;
import compiler.parsing.DataType;
import compiler.parsing.FunctionDefinition;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class FunctionDefinitionProcessor implements IProcessor {

    @Setter
    private FunctionDefinition functionDefinition;

    private int getParamsSize() throws CompileException {
        var size = 0;
        for (var param : functionDefinition.getFunctionParameters()) {
            switch (param.getDataType()) {
                case Int -> size += IntegerUtils.sizeOf();
                case Float -> size += FloatUtils.sizeOf();
                case Boolean -> size += BooleanUtils.sizeOf();
                default -> throw new CompileException("Error, invalid data type provided");
            }
        }
        return size;
    }

    @Override
    public void process(GeneratorContext parentContext) throws CompileException {
        var functionIdentifier = functionDefinition.getIdentifier();

        // Funkce by nemela existovat - jinak bysme generovali duplicitni kod
        if (parentContext.functionExists(functionIdentifier)) {
            throw new CompileException("Error, redefinition of function with identifier: " + functionIdentifier);
        }

        var totalSize = getParamsSize() + (functionDefinition.getReturnType() == DataType.Void
                ? 0
                : VariableUtils.getSizeOf(functionDefinition.getReturnType()));

        // Vytvorime misto pro argumenty a navratovou hodnotu
        parentContext.updateParamSpaceRequirements(totalSize);

        // Cislo instrukce
        var functionInstruction = parentContext.getNextInstructionNumber();
        var paramsAddress = GeneratorContext.getParamsAddressIdx();
        functionDefinition.setAddress(functionInstruction);
        functionDefinition.setParamsAddress(paramsAddress);

        // Vytvorime kontext pro funkci
        // Tento kontext neni pripojen k rodici, takze nebude moci referencovat promenne v rodici
        // tim zajistime, ze funkce se bude chovat jako skutecna "pure" funkce, ktera neni zavisla na
        // vnejsim stavu
        var context = new GeneratorContext(1, parentContext, false);
        context.setStackPointerAddress(0); // Reset stack pointeru protoze jsme ve funkci

        // Alokujeme 3 mista pro SB, DB, PC a pak jeste extra misto pro ukladani parametru pri volani funkci ve funkci
        // Toto by slo jeste optimalizovat, napr. kdybychom prosli vsechny statementy ve funkci a zjistili, co se vola
        // nicmene by to vyzadovalo velkou cast kodu navic, protoze aktualne tridy i rovnou vypisuji kod pri pruchodu,
        // takze to nechame takto a bude to omezeni jazyka
        context.addInstruction(PL0InstructionType.INT, 0, 3 + GeneratorContext.getParamsMaxSize());

        // Registrujeme identifikatory argumentu do noveho kontextu
        for (var functionParameter : functionDefinition.getFunctionParameters()) {
            context.allocateVariable(functionParameter.getIdentifier(), functionParameter.getDataType());

            // Zaroven promennou rovnou ziskame a nastavime ze je deklarovana a inicializovana
            var paramVariable = context.getVariableOrDefault(functionParameter.getIdentifier());
            paramVariable.setDeclared(true);
            paramVariable.setInitialized(true);

            // Nacteme hodnoty parametru do promennych
            VariableUtils.storeToParam(context, paramVariable, 1, paramsAddress);
            paramsAddress += VariableUtils.getSizeOf(paramVariable.getDataType()); // pricteme velikost datoveho typu
        }

        // Nyni staci pouze zpracovat statementy ve funkci
        // Generujeme samotny kod funkce
        var blockScopeProcessor = new FunctionBlockScopeProcessor(functionDefinition.getBlockScope(), true,
                parentContext.getStackLevel(), functionDefinition);
        blockScopeProcessor.process(context);


        // Pokud je navratova hodnota je ulozena na stacku - takze ji staci zkopirovat zpet na misto
        if (functionDefinition.getReturnType() != DataType.Void) {
            VariableUtils.storeToAddress(context, functionDefinition.getReturnType(), 1,
                    paramsAddress);
        }

        // Dealokovat misto musime az po tom co zapiseme return hodnotu
        blockScopeProcessor.deallocateSpace(context);

        // Zavolame return
        context.addInstruction(PL0InstructionType.RET, 0, 0);

        // Pridame funkci do seznamu
        context.addFunction(functionDefinition);
    }
}
