package parser.visitor;

import compiler.parsing.statement.Statement;
import main.antlr4.grammar.CMMParser;
import parser.visitor.BlockScopeVisitor;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ControlFlowVisitor;
import parser.visitor.function.FunctionCallVisitor;
import parser.visitor.variable.ConstVariableInitializationVisitor;
import parser.visitor.variable.VariableAssignmentVisitor;
import parser.visitor.variable.VariableDeclarationVisitor;
import parser.visitor.variable.VariableInitializationVisitor;

/**
 * Wrapper okolo vsech statement visitoru
 */
public class StatementVisitor extends CMMLevelAwareVisitor<Statement> {
    public StatementVisitor(long depth) {
        super(depth);
    }


    @Override
    public Statement visitBlockScope(CMMParser.BlockScopeContext ctx) {
        return new BlockScopeVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitIfStatement(CMMParser.IfStatementContext ctx) {
        return new ControlFlowVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitForStatement(CMMParser.ForStatementContext ctx) {
        return new ControlFlowVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitWhileStatement(CMMParser.WhileStatementContext ctx) {
        return new ControlFlowVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitDoWhileStatement(CMMParser.DoWhileStatementContext ctx) {
        return new ControlFlowVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitVariableDeclarationStatement(CMMParser.VariableDeclarationStatementContext ctx) {
        return new VariableDeclarationVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitVariableInitializationStatement(CMMParser.VariableInitializationStatementContext ctx) {
        return new VariableInitializationVisitor(depth, false).visit(ctx);
    }

    @Override
    public Statement visitConstVariableInitializationStatement(CMMParser.ConstVariableInitializationStatementContext ctx) {
        return new ConstVariableInitializationVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitVariableAssignmentStatement(CMMParser.VariableAssignmentStatementContext ctx) {
        return new VariableAssignmentVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitBlockOfCode(CMMParser.BlockOfCodeContext ctx) {
        return new BlockScopeVisitor(depth).visit(ctx);
    }

    @Override
    public Statement visitFunctionCallStatement(CMMParser.FunctionCallStatementContext ctx) {
        return new FunctionCallVisitor(depth).visit(ctx);
    }
}
