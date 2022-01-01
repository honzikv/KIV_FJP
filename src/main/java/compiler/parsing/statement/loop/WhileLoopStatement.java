package compiler.parsing.statement.loop;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.StatementType;
import lombok.ToString;

/**
 * While je vlastne uplne to same jako for, jenom se bude jinak prekladat
 */
@ToString
public class WhileLoopStatement extends ForLoopStatement {

    public WhileLoopStatement(long depth, Expression expression, BlockScope blockScope) {
        super(StatementType.WhileLoop, depth, expression, blockScope);
    }
}
