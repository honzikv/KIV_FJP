package parser.visitor.expression;

import compiler.language.expression.ValueExpression;
import compiler.language.variable.DataType;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class ValueExpressionVisitor extends CMMLevelAwareVisitor<ValueExpression> {

    public ValueExpressionVisitor(long depth) {
        super(depth);
    }

    @Override
    public ValueExpression visitValueExpression(CMMParser.ValueExpressionContext ctx) {
        var value = ctx.valueExpr().getText();
        return new ValueExpression(DataType.getDataTypeFromValue(value), value);
    }
}
