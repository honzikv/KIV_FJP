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
public class ReturnStatement  extends Statement {

    private final boolean isVoid;

    private final Expression expression;

    private final String value;

    public ReturnStatement(long depthLevel) {
        super(StatementType.ReturnStatement, depthLevel);
        this.isVoid = true;
        this.expression = null;
        this.value = null;
    }

    public ReturnStatement(long depthLevel, Expression expression) {
        super(StatementType.ReturnStatement, depthLevel);
        this.isVoid = false;
        this.expression = expression;
        this.value = null;
    }

    public ReturnStatement(long depthLevel, String value) {
        super(StatementType.ReturnStatement, depthLevel);
        this.isVoid = false;
        this.expression = null;
        this.value = value;
    }
}
