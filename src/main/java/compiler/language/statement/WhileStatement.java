package compiler.language.statement;

import compiler.language.expression.Expression;

/**
 * While je vlastne uplne to same jako for, jenom se bude jinak prekladat
 */
public class WhileStatement extends ForStatement {

    public WhileStatement(long depth, Expression expression, BlockScope blockScope) {
        super(StatementType.WhileLoop,depth, expression, blockScope);
    }
}
