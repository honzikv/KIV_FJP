package parser.visitor.function;

import compiler.parsing.expression.Expression;
import compiler.parsing.expression.IdentifierExpression;
import java.util.ArrayList;
import java.util.List;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ExpressionVisitor;

public class FunctionIdentifierChainVisitor extends CMMLevelAwareVisitor<List<Expression>> {
    public FunctionIdentifierChainVisitor(long depth) {
        super(depth);
    }

    @Override
    public List<Expression> visitExpressionChain(CMMParser.ExpressionChainContext ctx) {
        var result = new ArrayList<Expression>();

        var expression = ctx.IDENTIFIER() == null
                ? new ExpressionVisitor(depth).visit(ctx.expression())
                : new IdentifierExpression(ctx.IDENTIFIER().getText());

        result.add(expression);

        // Pokud neni chain null navstivime i ten
        if (ctx.expressionChain() != null) {
            result.addAll(visitExpressionChain(ctx.expressionChain()));
        }

        return result;
    }
}
