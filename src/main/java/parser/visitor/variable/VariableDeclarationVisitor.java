package parser.visitor.variable;

import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.parsing.DataType;
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
        var identifier = ctx.IDENTIFIER().getText();
        var dataType = DataType.convertStringTypeToDataType(ctx.legalDataTypes().getText());

        return new VariableDeclarationStatement(depth, identifier, dataType);
    }
}
