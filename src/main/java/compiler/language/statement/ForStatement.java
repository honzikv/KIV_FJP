package compiler.language.statement;

import compiler.language.expression.Expression;
import lombok.Getter;

/**
 * Reprezentuje for cyklus
 */
public class ForStatement extends Statement {

    @Getter
    private final Expression expression;

    @Getter
    private final BlockScope blockScope;

    /**
     * Konstruktor pro for cyklus
     * @param depth hloubka zanoreni
     * @param expression vyraz ve for cyklu
     * @param blockScope scope s vykonnym kodem
     */
    public ForStatement(long depth, Expression expression, BlockScope blockScope) {
        super(StatementType.ForLoop, depth);
        this.expression = expression;
        this.blockScope = blockScope;
    }

    /**
     * Pro While a Do While cykly
     * @param statementType typ statementu
     * @param depth hloubka
     * @param expression vyraz
     * @param blockScope scope s kodem
     */
    protected ForStatement(StatementType statementType, long depth, Expression expression, BlockScope blockScope) {
        super(statementType, depth);
        this.expression = expression;
        this.blockScope = blockScope;
    }
}
