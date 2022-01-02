package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.function.FunctionCallProcessor;
import compiler.parsing.expression.FunctionCallExpression;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FunctionCallExpressionProcessor implements IProcessor {

    private final FunctionCallExpression functionCallExpression;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var functionCall = functionCallExpression.getFunctionCall();
        var functionIdentifier = functionCall.getIdentifier();

        if (!context.functionExists(functionIdentifier)) {
            throw new CompileException("Error, calling a function that has not been defined! (" + functionIdentifier + ")");
        }
        var function = context.getFunction(functionIdentifier);

        // Vytvorime processor pro function call a zpracujeme
        var functionCallProcessor = new FunctionCallProcessor(functionCall);
        functionCallProcessor.process(context);

        // Nastavime return type
        functionCallExpression.setDataType(function.getReturnType());
    }
}
