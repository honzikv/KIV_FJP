package parser.visitor.statement.variable;


import compiler.language.statement.variable.VariableInitializationStatement;
import compiler.language.variable.DataType;
import java.util.ArrayList;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ExpressionVisitor;

public class VariableInitializationVisitor extends CMMLevelAwareVisitor<VariableInitializationStatement> {

    /**
     * Pouziva se pro konstatni promenne
     */
    private final boolean useConst;

    public VariableInitializationVisitor(long depth, boolean useConst) {
        super(depth);
        this.useConst = useConst;
    }

    @Override
    public VariableInitializationStatement visitVariableInitializationStatement(
            CMMParser.VariableInitializationStatementContext ctx) {
        return visitVariableInitialization(ctx.variableInitialization());
    }

    @Override
    public VariableInitializationStatement visitVariableInitialization(CMMParser.VariableInitializationContext ctx) {
        var dataType = DataType.convertStringTypeToDataType(ctx.legalDataTypes().getText());
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
            return new VariableInitializationStatement(depth, dataType, identifier, chainedIdentifiers, expression,
                    useConst);
        }

        return new VariableInitializationStatement(depth, dataType, identifier, chainedIdentifiers,
                ctx.legalVariableLiterals().getText(), useConst);
    }


}
