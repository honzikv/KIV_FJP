package compiler.parsing.statement;

import compiler.parsing.expression.Expression;
import lombok.Getter;
import lombok.ToString;

/**
 * Reprezentuje for cyklus
 */
@ToString
@Getter
public class ForStatement extends Statement {

    private final Expression expression;

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
