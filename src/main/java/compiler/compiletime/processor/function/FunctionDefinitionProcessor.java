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

    private static final int FunctionSize = 3; // SB, DB, PC a SP

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
        var paramsAddress = parentContext.getStackPointerAddress(); // adresa s parametry na stacku
        parentContext.addInstruction(PL0InstructionType.INT, 0, totalSize);

        // Adresa funkce pro volani
        var functionAddress = parentContext.getNextInstructionNumber();
        functionDefinition.setAddress(functionAddress);
        functionDefinition.setParamsAddress(paramsAddress);

        // Vytvorime kontext pro funkci
        // Tento kontext neni pripojen k rodici, takze nebude moci referencovat promenne v rodici
        // tim zajistime, ze funkce se bude chovat jako skutecna "pure" funkce, ktera neni zavisla na
        // vnejsim stavu
        var context = new GeneratorContext(1, parentContext, false);
        context.setStackPointerAddress(0); // Reset stack pointeru protoze jsme ve funkci

        // Registrujeme identifikatory argumentu do noveho kontextu
        for (var functionParameter : functionDefinition.getFunctionParameters()) {
            context.allocateVariable(functionParameter.getIdentifier(), functionParameter.getDataType());

            // Zaroven promennou rovnou ziskame a nastavime ze je deklarovana a inicializovana
            var paramVariable = context.getVariableOrDefault(functionParameter.getIdentifier());
            paramVariable.setDeclared(true);
            paramVariable.setInitialized(true);

            // Nacteme hodnoty parametru do promennych
            VariableUtils.storeToVariable(context, paramVariable, 0, paramsAddress);
            paramsAddress += VariableUtils.getSizeOf(paramVariable.getDataType()); // pricteme velikost datoveho typu
        }

        context.addInstruction(PL0InstructionType.INT, 0, 4);

        // Nyni staci pouze zpracovat statementy ve funkci
        // Generujeme samotny kod funkce
        var blockScopeProcessor = new FunctionBlockScopeProcessor(functionDefinition.getBlockScope(), true,
                parentContext.getStackLevel(), functionDefinition);
        blockScopeProcessor.process(context);

        // Pokud je navratova hodnota je ulozena na stacku - takze ji staci zkopirovat zpet na misto
        if (functionDefinition.getReturnType() != DataType.Void) {
            VariableUtils.storeToAddress(context, functionDefinition.getReturnType(), 1,
                    paramsAddress + getParamsSize());
        }

        // Nacteme adresu, kam se mame vratit z predchoziho stacku - ta je vzdy ulozena na tretim indexu
        context.addInstruction(PL0InstructionType.LOD, 1, 3);

        // A ulozime ji na program counter
        context.addInstruction(PL0InstructionType.STO, 0, 2);

        // Zavolame return
        context.addInstruction(PL0InstructionType.RET, 0, 0);

        // Pridame funkci do seznamu
        context.addFunction(functionDefinition);
    }
}
