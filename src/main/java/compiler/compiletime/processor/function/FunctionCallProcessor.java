package compiler.compiletime.processor.function;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.compiletime.utils.DataTypeUtils;
import compiler.parsing.DataType;
import compiler.parsing.statement.function.FunctionCall;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FunctionCallProcessor implements IProcessor {

    private final FunctionCall functionCall;

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

        var returnAddress = Long.MIN_VALUE;
        // Pro vraceni dane hodnoty musime na stacku vytvorit prazdne misto, kam se hodnota pozdeji zapise
        if (function.getReturnType() != DataType.Void) {
            returnAddress = context.getStackPointerAddress();
            DataTypeUtils.addSpaceOnStack(context, function.getReturnType());
        }

        // Zpracujeme parametry
        var expressionProcessor = new ExpressionProcessor(); // pro zpracovani expressionu
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
        }

        // Nyni mame na stacku vsechny parametry, takze muzeme zavolat funkci
        context.addInstruction(PL0InstructionType.CAL, context.getStackLevel(), function.getAddress());

        if (function.getReturnType() != DataType.Void) {
            context.addInstruction(PL0InstructionType.LOD, context.getStackLevel(), returnAddress);
        }
    }
}
