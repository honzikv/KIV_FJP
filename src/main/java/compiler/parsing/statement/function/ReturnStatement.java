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

    public ReturnStatement(long depthLevel) {
        super(StatementType.ReturnStatement, depthLevel);
        this.expression = null;
    }

    public ReturnStatement(long depthLevel, Expression expression) {
        super(StatementType.ReturnStatement, depthLevel);
        this.expression = expression;
    }

}
