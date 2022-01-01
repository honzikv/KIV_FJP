package compiler.compiletime.processor.function;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
import compiler.parsing.FunctionDefinition;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class FunctionDefinitionProcessor implements IProcessor {

    private static final int FunctionSize = 3;

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
    public void process(GeneratorContext context) throws CompileException {
        var functionIdentifier = functionDefinition.getIdentifier();
        if (context.functionExists(functionIdentifier)) {
            throw new CompileException("Error, redefinition of function with identifier: " + functionIdentifier);
        }

        var totalSize = FunctionSize + getParamsSize();

        // Vytvorime misto pro argumenty a funkci samotnou
        context.addInstruction(PL0InstructionType.INT, 0, 3);


    }
}
