package compiler.parsing.statement.loop;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.StatementType;

public class RepeatUntilLoopStatement extends WhileLoopStatement {

    public RepeatUntilLoopStatement(long depth, Expression expression, BlockScope blockScope) {
        super(StatementType.RepeatUntilLoop, depth, expression, blockScope);
    }
}
