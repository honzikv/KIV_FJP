package parser.visitor.function;

import compiler.parsing.DataType;
import compiler.parsing.expression.ValueExpression;
import compiler.parsing.statement.function.ReturnStatement;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.ExpressionVisitor;

public class ReturnStatementVisitor extends CMMLevelAwareVisitor<ReturnStatement> {

    public ReturnStatementVisitor(long depth) {
        super(depth);
    }

    @Override
    public ReturnStatement visitReturnStatement(CMMParser.ReturnStatementContext ctx) {

        if (ctx == null) {
            return null;
        }

        if (ctx.IDENTIFIER() != null) {
            return new ReturnStatement(depth, ctx.IDENTIFIER().getText());
        }

        if (ctx.legalVariableLiterals() != null) {
            var valueExpression = ctx.legalVariableLiterals().getText();
            return new ReturnStatement(depth, new ValueExpression(DataType.getDataTypeFromValue(valueExpression), valueExpression));
        }

        // Jinak je to neterminal expression

        if (ctx.expression() != null) {
            return new ReturnStatement(depth, new ExpressionVisitor(depth).visit(ctx.expression()));
        }

        return new ReturnStatement(depth); // void
    }
}
