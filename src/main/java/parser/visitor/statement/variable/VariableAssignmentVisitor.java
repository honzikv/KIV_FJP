package parser.visitor.statement.variable;

import compiler.language.statement.variable.VariableAssignmentStatement;
import java.util.ArrayList;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ExpressionVisitor;

public class VariableAssignmentVisitor extends CMMLevelAwareVisitor<VariableAssignmentStatement> {

    public VariableAssignmentVisitor(long depth) {
        super(depth);
    }

    @Override
    public VariableAssignmentStatement visitVariableAssignmentStatement(CMMParser.VariableAssignmentStatementContext ctx) {
        return visitVariableAssignment(ctx.variableAssignment());
    }

    @Override
    public VariableAssignmentStatement visitVariableAssignment(CMMParser.VariableAssignmentContext ctx) {
        var identifier = ctx.IDENTIFIER().getText();
        var chainedIdentifiers = new ArrayList<String>();
        if (ctx.chainAssignment() != null) {
            var chainAssignmentVisitor = new ChainAssignmentVisitor();
            ctx.chainAssignment().forEach(chainAssignmentContext ->
                    chainedIdentifiers.add(chainAssignmentVisitor.visit(chainAssignmentContext)));
        }

        if (ctx.expression() != null) {
            // mame expression tzn musime ho resolvovat
            var expression = new ExpressionVisitor(depth).visit(ctx.expression());
            return new VariableAssignmentStatement(depth, identifier, chainedIdentifiers, expression);
        }

        return new VariableAssignmentStatement(depth, identifier, chainedIdentifiers,
                ctx.legalVariableLiterals().getText());
    }
}
