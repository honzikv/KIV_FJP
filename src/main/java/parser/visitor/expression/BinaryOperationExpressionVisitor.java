package parser.visitor.expression;

import compiler.language.expression.BinaryOperationExpression;
import compiler.language.expression.OperationType;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class BinaryOperationExpressionVisitor extends CMMLevelAwareVisitor<BinaryOperationExpression> {

    public BinaryOperationExpressionVisitor(long depth) {
        super(depth);
    }

    @Override
    public BinaryOperationExpression visitMultiplicationExpression(CMMParser.MultiplicationExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public BinaryOperationExpression visitAdditionExpression(CMMParser.AdditionExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public BinaryOperationExpression visitComparisonExpression(CMMParser.ComparisonExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public BinaryOperationExpression visitBooleanOperationExpression(CMMParser.BooleanOperationExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }
}
