package compiler.parsing.statement.function;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import lombok.Getter;
import lombok.ToString;

/**
 * Return statement ve funkci
 */
@Getter
@ToString
public class ReturnStatement extends Statement {


    private final Expression expression;

    private final String value;

    private final boolean isLiteralValue;

    public ReturnStatement(long depthLevel) {
        super(StatementType.ReturnStatement, depthLevel);
        this.expression = null;
        this.value = null;
        this.isLiteralValue = false;
    }

    public ReturnStatement(long depthLevel, Expression expression) {
        super(StatementType.ReturnStatement, depthLevel);
        this.expression = expression;
        this.value = null;
        this.isLiteralValue = false;
    }

    public ReturnStatement(long depthLevel, String value) {
        super(StatementType.ReturnStatement, depthLevel);
        this.expression = null;
        this.value = value;
        this.isLiteralValue = true;
    }
}
