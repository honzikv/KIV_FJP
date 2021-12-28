package parser.visitor.statement.function;

import compiler.language.statement.function.FunctionCall;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class FunctionCallVisitor extends CMMLevelAwareVisitor<FunctionCall> {
    public FunctionCallVisitor(long depth) {
        super(depth);
    }

    @Override
    public FunctionCall visitFunctionCallStatement(CMMParser.FunctionCallStatementContext ctx) {
        return super.visitFunctionCall(ctx.functionCall());
    }

    @Override
    public FunctionCall visitFunctionCall(CMMParser.FunctionCallContext ctx) {
        var identifier = ctx.identifier().getText();
        var params = new FunctionIdentifierChainVisitor(depth).visit(ctx.identifierChain());

        return new FunctionCall(depth, identifier, params);
    }
}
