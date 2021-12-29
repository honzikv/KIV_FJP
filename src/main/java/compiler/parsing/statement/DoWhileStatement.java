package compiler.parsing.statement;

import compiler.parsing.expression.Expression;
import lombok.Getter;
import lombok.ToString;

/**
 * Do While je to same jako while, jenom se bude jinak prekladat
 */
@ToString
@Getter
public class DoWhileStatement extends ForStatement {

    public DoWhileStatement(long depthLevel, Expression expression, BlockScope blockScope) {
        super(StatementType.DoWhileLoop ,depthLevel, expression, blockScope);
    }
}
