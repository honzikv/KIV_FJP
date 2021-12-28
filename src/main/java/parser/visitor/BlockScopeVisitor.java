package parser.visitor;

import compiler.language.statement.BlockScope;
import main.antlr4.grammar.CMMParser;

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
}
