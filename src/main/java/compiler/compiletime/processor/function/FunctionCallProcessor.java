package compiler.compiletime.processor.function;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.compiletime.utils.VariableUtils;
import compiler.parsing.DataType;
import compiler.parsing.statement.function.FunctionCall;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FunctionCallProcessor implements IProcessor {

    private final FunctionCall functionCall;

    private static final int ReturnAddressPosition = 3;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var identifier = functionCall.getIdentifier();

        if (!context.functionExists(identifier)) {
            throw new CompileException("Error, called function: " + identifier + " does not exist!");
        }

        // Ziskame funkci
        var function = context.getFunction(identifier);

        // Pokud neni pocet parametru stejny, vyhodime exception
        if (function.getFunctionParameters().size() != functionCall.getParameters().size()) {
            throw new CompileException("Error, different number of parameters between function " +
                    "definition and function call " + "( " + function.getIdentifier() + " )");
        }

        // Zpracujeme parametry
        var expressionProcessor = new ExpressionProcessor(); // pro zpracovani expressionu
        var paramsAddress = function.getParamsAddress();
        for (var i = 0; i < functionCall.getParameters().size(); i += 1) {
            // Expression bude mit automaticky nastaveny typ, takze muzeme rovnou validovat proti funkci
            var expression = functionCall.getParameters().get(i);
            var formalParam = function.getFunctionParameters().get(i);
            expressionProcessor.process(context, expression);

            if (expression.getDataType() != formalParam.getDataType()) {
                throw new CompileException("Error, function parameter value data type differs from declaration ("
                        + " expected " + formalParam.getDataType().getStringValue() + " got "
                        + expression.getDataType().getStringValue() + " )");
            }

            // Ulozime parametr
            VariableUtils.storeToAddress(context, formalParam.getDataType(), 0, paramsAddress);
            paramsAddress += VariableUtils.getSizeOf(formalParam.getDataType()); // posuneme se
        }

        // Nyni mame na stacku vsechny parametry, takze muzeme zavolat funkci
        context.addInstruction(PL0InstructionType.CAL, 0, function.getAddress());

//        context.getInstruction(returnInstructionIdx).setInstructionAddress(context.getNextInstructionNumber());
        // Pokud funkce neco vracela nacteme to na stack
        if (function.getReturnType() != DataType.Void) {
            // Po iteraci pres zapis vsech parametru je nyni adresa nastavena na adresu vystupu funkce
            context.addInstruction(PL0InstructionType.LOD, 0, paramsAddress); // TODO co tady ma bejt
        }

    }
}
