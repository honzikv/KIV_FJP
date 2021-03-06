package parser.visitor;

import compiler.parsing.DataType;
import compiler.parsing.expression.BinaryOperationExpression;
import compiler.parsing.expression.Expression;
import compiler.parsing.expression.FunctionCallExpression;
import compiler.parsing.expression.IdentifierExpression;
import compiler.parsing.expression.InstanceOfExpression;
import compiler.parsing.expression.OperationType;
import compiler.parsing.expression.UnaryOperationExpression;
import compiler.parsing.expression.ValueExpression;
import main.antlr4.grammar.CMMParser;
import parser.visitor.function.FunctionCallVisitor;

/**
 * Visitor pro expression
 */
public class ExpressionVisitor extends CMMLevelAwareVisitor<Expression> {

    public ExpressionVisitor(long depth) {
        super(depth);
    }

    @Override
    public Expression visitParenthesesExpression(CMMParser.ParenthesesExpressionContext ctx) {
        return visit(ctx.expression());
    }


    @Override
    public Expression visitParenthesizedExpression(CMMParser.ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Expression visitMultiplicationExpression(CMMParser.MultiplicationExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public Expression visitAdditionExpression(CMMParser.AdditionExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public Expression visitComparisonExpression(CMMParser.ComparisonExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public Expression visitBooleanOperationExpression(CMMParser.BooleanOperationExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression(0));
        var expressionRight = new ExpressionVisitor(depth).visit(ctx.expression(1));
        var operation = OperationType.getBinaryOperationType(ctx.operation.getText());

        return new BinaryOperationExpression(expressionLeft, expressionRight,
                operation);
    }

    @Override
    public Expression visitUnaryMinusExpression(CMMParser.UnaryMinusExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression());
        var operation = OperationType.getUnaryOperationType(ctx.operation.getText());

        return new UnaryOperationExpression(expressionLeft, operation);
    }

    @Override
    public Expression visitNegationExpression(CMMParser.NegationExpressionContext ctx) {
        var expressionLeft = new ExpressionVisitor(depth).visit(ctx.expression());
        var operation = OperationType.getUnaryOperationType(ctx.NOT().getText());

        return new UnaryOperationExpression(expressionLeft, operation);
    }

    @Override
    public Expression visitValueExpression(CMMParser.ValueExpressionContext ctx) {
        var value = ctx.valueExpr().getText();
        return new ValueExpression(DataType.getDataTypeFromValue(value), value);
    }

    @Override
    public Expression visitIdentifierExpression(CMMParser.IdentifierExpressionContext ctx) {
        return new IdentifierExpression(ctx.IDENTIFIER().getText());
    }

    @Override
    public Expression visitFunctionCallExpression(CMMParser.FunctionCallExpressionContext ctx) {
        var functionCall = new FunctionCallVisitor(depth).visit(ctx.functionCall());
        return new FunctionCallExpression(functionCall);
    }

    @Override
    public Expression visitInstanceOfExpression(CMMParser.InstanceOfExpressionContext ctx) {
        var identifier = ctx.IDENTIFIER() == null
                ? ctx.legalDataTypes(0).getText()
                : ctx.IDENTIFIER().getText();
        var dataTypeCtx = ctx.IDENTIFIER() == null
                ? ctx.legalDataTypes(1)
                : ctx.legalDataTypes(0);
        var dataType = DataType.convertStringTypeToDataType(dataTypeCtx.getText());

        return new InstanceOfExpression(identifier, dataType);
    }
}
