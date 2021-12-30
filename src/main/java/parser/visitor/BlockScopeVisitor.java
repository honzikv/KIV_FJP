package parser.visitor;

import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.function.FunctionBlockScope;
import main.antlr4.grammar.CMMParser;
import parser.visitor.function.ReturnStatementVisitor;

public class BlockScopeVisitor extends CMMLevelAwareVisitor<BlockScope> {
    public BlockScopeVisitor(long depth) {
        super(depth);
    }

    @Override
    public BlockScope visitBlockScope(CMMParser.BlockScopeContext ctx) {
        var blockScope = new BlockScope(depth + 1);
        ctx.statement().forEach(statementCtx ->
                blockScope.addStatement(new StatementVisitor(depth + 1).visit(statementCtx)));

        return blockScope;
    }

    @Override
    public BlockScope visitFunctionBlockScope(CMMParser.FunctionBlockScopeContext ctx) {
        var returnStatement = new ReturnStatementVisitor(depth).visit(ctx.returnStatement());

        var blockScope = new FunctionBlockScope(depth + 1, returnStatement);

        if (ctx.statement() != null) {
            var statementVisitor = new StatementVisitor(depth);
            ctx.statement().forEach(statementContext ->
                    blockScope.addStatement(statementVisitor.visit(statementContext)));
        }

        return blockScope;
    }
}
