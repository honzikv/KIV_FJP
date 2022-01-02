package compiler.compiletime.processor.expression;

import compiler.compiletime.DataTypeParseUtils;
import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
import compiler.parsing.DataType;
import compiler.parsing.expression.ValueExpression;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValueExpressionProcessor implements IProcessor {

    private final ValueExpression expression;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var value = expression.getValue();

        var valueAsInt = DataTypeParseUtils.getIntegerOrDefault(value);
        if (valueAsInt != null) {
            expression.setDataType(DataType.Int);
            IntegerUtils.addOnStack(context, valueAsInt);
            return;
        }

        var valueAsFloat = DataTypeParseUtils.getFloatOrDefault(value);
        if (valueAsFloat != null) {
            expression.setDataType(DataType.Float);
            FloatUtils.addOnStack(context, valueAsFloat);
            return;
        }

        var valueAsBool = DataTypeParseUtils.getBooleanOrDefault(value);
        if (valueAsBool != null) {
            expression.setDataType(DataType.Boolean);
            BooleanUtils.addOnStack(context, valueAsBool);
            return;
        }

        throw new CompileException("Error, unsupported value!");
    }

}
