package compiler.parsing.statement;

import compiler.parsing.expression.Expression;
import lombok.ToString;

/**
 * While je vlastne uplne to same jako for, jenom se bude jinak prekladat
 */
@ToString
public class WhileStatement extends ForStatement {

    public WhileStatement(long depth, Expression expression, BlockScope blockScope) {
        super(StatementType.WhileLoop,depth, expression, blockScope);
    }
}
