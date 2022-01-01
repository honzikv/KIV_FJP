package parser.visitor.function;

import compiler.parsing.expression.Expression;
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

        // Navstivime expression
        var expression = new ExpressionVisitor(depth).visit(ctx.expression());
        result.add(expression);

        // Pokud neni chain null navstivime i ten
        if (ctx.expressionChain() != null) {
            result.addAll(visitExpressionChain(ctx.expressionChain()));
        }

        return result;
    }
}
