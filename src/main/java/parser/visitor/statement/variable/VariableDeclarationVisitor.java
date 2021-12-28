package parser.visitor.statement.variable;

import compiler.language.statement.variable.VariableDeclarationStatement;
import compiler.language.variable.DataType;
import java.util.Locale;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

/**
 * Visitor pro promenne
 */
public class VariableDeclarationVisitor extends CMMLevelAwareVisitor<VariableDeclarationStatement> {

    public VariableDeclarationVisitor(long depth) {
        super(depth);
    }

    @Override
    public VariableDeclarationStatement visitVariableDeclarationStatement(
            CMMParser.VariableDeclarationStatementContext ctx) {
        return visitVariableDeclaration(ctx.variableDeclaration());
    }

    @Override
    public VariableDeclarationStatement visitVariableDeclaration(CMMParser.VariableDeclarationContext ctx) {
        var identifier = ctx.identifier().getText();
        var dataType = DataType.fromString(ctx.legalDataTypes().getText());

        return new VariableDeclarationStatement(depth, identifier, dataType);
    }
}
