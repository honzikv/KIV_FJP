package parser.visitor.variable;


import compiler.parsing.DataType;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ExpressionVisitor;

/**
 * Visitor pro inicializaci promennych
 */
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

        if (ctx.expression() != null) {
            // mame expression tzn musime ho resolvovat
            var expression = new ExpressionVisitor(depth).visit(ctx.expression());
            return new VariableInitializationStatement(depth, dataType, identifier, expression,
                    useConst);
        }

        return new VariableInitializationStatement(depth, dataType, identifier,
                ctx.legalVariableLiterals().getText(), useConst);
    }


}
