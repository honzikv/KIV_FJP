package parser.visitor.function;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.function.FunctionCall;
import java.util.ArrayList;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class FunctionCallVisitor extends CMMLevelAwareVisitor<FunctionCall> {
    public FunctionCallVisitor(long depth) {
        super(depth);
    }

    @Override
    public FunctionCall visitFunctionCallStatement(CMMParser.FunctionCallStatementContext ctx) {
        return visitFunctionCall(ctx.functionCall());
    }

    @Override
    public FunctionCall visitFunctionCall(CMMParser.FunctionCallContext ctx) {
        var identifier = ctx.IDENTIFIER().getText();

        var params = ctx.expressionChain() == null
                ? new ArrayList<Expression>()
                : new FunctionIdentifierChainVisitor(depth).visit(ctx.expressionChain());

        return new FunctionCall(depth, identifier, params);
    }
}
