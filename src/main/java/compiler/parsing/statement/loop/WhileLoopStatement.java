package compiler.parsing.statement.loop;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import lombok.Getter;
import lombok.ToString;

/**
 * While extenduji podobne verze jako do-while a repeat until
 */
@ToString
@Getter
public class WhileLoopStatement extends Statement {

    private final Expression expression;

    private final BlockScope blockScope;

    public WhileLoopStatement(long depth, Expression expression, BlockScope blockScope) {
        super(StatementType.WhileLoop, depth);
        this.expression = expression;
        this.blockScope = blockScope;
    }

    protected WhileLoopStatement(StatementType statementType, long depth, Expression expression, BlockScope blockScope) {
        super(statementType, depth);
        this.expression = expression;
        this.blockScope = blockScope;
    }
}
