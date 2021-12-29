package parser.visitor.statement;

import compiler.language.statement.DoWhileStatement;
import compiler.language.statement.ForStatement;
import compiler.language.statement.IfStatement;
import compiler.language.statement.Statement;
import compiler.language.statement.WhileStatement;
import main.antlr4.grammar.CMMParser;
import parser.visitor.BlockScopeVisitor;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ExpressionVisitor;

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

        return new ForStatement(depth, expression, blockScope);
    }

    @Override
    public Statement visitWhileStatement(CMMParser.WhileStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.parenthesesExpression());
        var blockScope = new BlockScopeVisitor(depth).visit(ctx.blockScope());

        return new WhileStatement(depth, expression, blockScope);
    }

    @Override
    public Statement visitDoWhileStatement(CMMParser.DoWhileStatementContext ctx) {
        var expression = new ExpressionVisitor(depth).visit(ctx.parenthesesExpression());
        var blockScope = new BlockScopeVisitor(depth).visit(ctx.blockScope());

        return new DoWhileStatement(depth, expression, blockScope);
    }

}
