package parser.visitor.expression;

import compiler.language.expression.Expression;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

/**
 * Visitor pro expression
 */
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
