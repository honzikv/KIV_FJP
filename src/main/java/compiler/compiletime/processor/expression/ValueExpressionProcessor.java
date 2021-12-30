package compiler.compiletime.processor.expression;

import compiler.compiletime.DataTypeParseUtils;
import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.types.BooleanDataType;
import compiler.compiletime.types.FloatDataType;
import compiler.compiletime.types.IntegerDataType;
import compiler.compiletime.types.StringDataType;
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
            IntegerDataType.addOnStack(context, valueAsInt);
            return;
        }

        var valueAsFloat = DataTypeParseUtils.getFloatOrDefault(value);
        if (valueAsFloat != null) {
            expression.setDataType(DataType.Float);
            FloatDataType.addOnStack(context, valueAsFloat);
            return;
        }

        var valueAsBool = DataTypeParseUtils.getBooleanOrDefault(value);
        if (valueAsBool != null) {
            expression.setDataType(DataType.Boolean);
            BooleanDataType.addOnStack(context, valueAsBool);
            return;
        }

        // String je cokoliv jineho co proslo parserem
        expression.setDataType(DataType.String);
        StringDataType.addOnStack(context, value);
    }

}
