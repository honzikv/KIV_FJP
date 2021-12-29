package parser.visitor.statement.function;

import compiler.language.expression.ValueExpression;
import compiler.language.statement.function.ReturnStatement;
import compiler.language.variable.DataType;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;
import parser.visitor.expression.ExpressionVisitor;

public class ReturnStatementVisitor extends CMMLevelAwareVisitor<ReturnStatement> {

    public ReturnStatementVisitor(long depth) {
        super(depth);
    }

    @Override
    public ReturnStatement visitReturnStatement(CMMParser.ReturnStatementContext ctx) {
        if (ctx.VOID() != null) {
            return new ReturnStatement(depth);
        }

        if (ctx.IDENTIFIER() != null) {
            return new ReturnStatement(depth, ctx.IDENTIFIER().getText());
        }

        if (ctx.legalVariableLiterals().getText() != null) {
            var valueExpression = ctx.legalVariableLiterals().getText();
            return new ReturnStatement(depth, new ValueExpression(DataType.getDataTypeFromValue(valueExpression), valueExpression));
        }

        // Jinak je to neterminal expression
        return new ReturnStatement(depth, new ExpressionVisitor(depth).visit(ctx.expression()));
    }
}
