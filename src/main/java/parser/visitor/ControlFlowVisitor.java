package parser.visitor;

import compiler.parsing.statement.IfStatement;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.loop.DoWhileLoopStatement;
import compiler.parsing.statement.loop.ForLoopStatement;
import compiler.parsing.statement.loop.WhileLoopStatement;
import main.antlr4.grammar.CMMParser;

/**
 * Visitor pro control flow - tzn. cykly, if, apod.
 */
public class ControlFlowVisitor extends CMMLevelAwareVisitor<Statement> {


    public ControlFlowVisitor(long depth) {
        super(depth);
    }

    @Override
    public Statement visitIfStatement(CMMParser.IfStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.parenthesesExpression());
        var ifScope = new BlockScopeVisitor(depth).visit(ctx.blockScope().get(0));
        var elseScope = ctx.blockScope().size() > 1
                ? new BlockScopeVisitor(depth).visit(ctx.blockScope().get(1))
                : null;

        return new IfStatement(depth, expression, ifScope, elseScope);
    }

    @Override
    public Statement visitForStatement(CMMParser.ForStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.forExpression());
        var blockScope = new BlockScopeVisitor(depth).visit(ctx.blockScope());

        return new ForLoopStatement(depth, expression, blockScope);
    }

    @Override
    public Statement visitWhileStatement(CMMParser.WhileStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.parenthesesExpression());
        var blockScope = new BlockScopeVisitor(depth).visit(ctx.blockScope());

        return new WhileLoopStatement(depth, expression, blockScope);
    }

    @Override
    public Statement visitDoWhileStatement(CMMParser.DoWhileStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.parenthesesExpression());
        var blockScope = new BlockScopeVisitor(depth).visit(ctx.blockScope());

        return new DoWhileLoopStatement(depth, expression, blockScope);
    }

}
