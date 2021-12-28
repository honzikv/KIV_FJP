package parser.visitor;

import compiler.language.expression.Expression;
import main.antlr4.grammar.CMMParser;

public class ExpressionVisitor extends CMMLevelAwareVisitor<Expression> {

    public ExpressionVisitor(long depth) {
        super(depth);
    }

    @Override
    public Expression visitParenthesesExpression(CMMParser.ParenthesesExpressionContext ctx) {
        return new ExpressionVisitor(depth).visit(ctx.expression());
    }



    @Override
    public Expression visitParenthesizedExpression(CMMParser.ParenthesizedExpressionContext ctx) {
        return super.visitParenthesizedExpression(ctx);
    }
}
