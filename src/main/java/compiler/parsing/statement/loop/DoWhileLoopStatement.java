package compiler.parsing.statement.loop;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.StatementType;
import lombok.Getter;
import lombok.ToString;

/**
 * Do While je to same jako while, jenom se bude jinak prekladat
 */
@ToString
@Getter
public class DoWhileLoopStatement extends ForLoopStatement {

    public DoWhileLoopStatement(long depthLevel, Expression expression, BlockScope blockScope) {
        super(StatementType.DoWhileLoop, depthLevel, expression, blockScope);
    }
}
