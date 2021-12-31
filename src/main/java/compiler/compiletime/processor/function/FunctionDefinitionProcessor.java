package compiler.compiletime.processor.function;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.libs.BooleanLib;
import compiler.compiletime.libs.FloatLib;
import compiler.compiletime.libs.IntegerLib;
import compiler.parsing.FunctionDefinition;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FunctionDefinitionProcessor implements IProcessor {

    private static final int FunctionSize = 3;
    private final FunctionDefinition functionDeclaration;

    private int getParamsSize() throws CompileException {
        var size = 0;
        for (var param : functionDeclaration.getFunctionParameters()) {
            switch (param.getDataType()) {
                case Int -> size += IntegerLib.sizeOf();
                case Float -> size += FloatLib.sizeOf();
                case Boolean -> size += BooleanLib.sizeOf();
                default -> throw new CompileException("Error, invalid data type provided");
            }
        }
        return size;
    }

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var functionIdentifier = functionDeclaration.getIdentifier();
        if (context.functionExists(functionIdentifier)) {
            throw new CompileException("Error, redefinition of function with identifier: " + functionIdentifier);
        }

        var totalSize = FunctionSize + getParamsSize();

    }
}
