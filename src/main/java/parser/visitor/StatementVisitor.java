package parser.visitor;

import compiler.language.statement.ForStatement;
import compiler.language.statement.IfStatement;
import compiler.language.statement.Statement;
import main.antlr4.grammar.CMMParser;

public class StatementVisitor extends CMMLevelAwareVisitor<Statement> {


    public StatementVisitor(long depth) {
        super(depth);
    }

    @Override
    public Statement visitIfStatement(CMMParser.IfStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.parenthesesExpression());
        var ifScope = new BlockScopeVisitor(depth).visit(ctx.blockScope().get(0));
        var elseScope = ctx.blockScope().get(1) != null
                ? new BlockScopeVisitor(depth).visit(ctx.blockScope().get(1))
                : null;

        return new IfStatement(depth, expression, ifScope, elseScope);
    }

    @Override
    public Statement visitForStatement(CMMParser.ForStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.forExpression());
        var blockScope = new BlockScopeVisitor(depth).visit(ctx.blockScope());

        return new ForStatement(depth, expression, blockScope);
    }

    @Override
    public Statement visitWhileStatement(CMMParser.WhileStatementContext ctx) {
        return super.visitWhileStatement(ctx);
    }
}
