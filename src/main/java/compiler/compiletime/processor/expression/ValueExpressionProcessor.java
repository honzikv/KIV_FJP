package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.expression.ValueExpression;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValueExpressionProcessor implements IProcessor {

    private final ValueExpression valueExpression;

    @Override
    public void process(GeneratorContext context) throws CompileException {

    }
}
