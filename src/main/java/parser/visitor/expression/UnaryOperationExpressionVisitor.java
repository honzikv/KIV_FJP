package parser.visitor.expression;

import compiler.language.expression.OperationType;
import compiler.language.expression.UnaryOperationExpression;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class UnaryOperationExpressionVisitor extends CMMLevelAwareVisitor<UnaryOperationExpression> {

    public UnaryOperationExpressionVisitor(long depth) {
        super(depth);
    }

    @Override
    public UnaryOperationExpression visitUnaryMinusExpression(CMMParser.UnaryMinusExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression());
        var operation = OperationType.getUnaryOperationType(ctx.operation.getText());

        return new UnaryOperationExpression(expressionLeft, operation);
    }

    @Override
    public UnaryOperationExpression visitNegationExpression(CMMParser.NegationExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression());
        var operation = OperationType.getUnaryOperationType(ctx.NOT().getText());

        return new UnaryOperationExpression(expressionLeft, operation);
    }
}
