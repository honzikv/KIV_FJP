package compiler.parsing.statement;

import compiler.parsing.expression.Expression;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class IfStatement extends Statement {

    /**
     * Vyraz v ifu
     */
    private final Expression expression;

    /**
     * Vykonny kod v ifu
     */
    private final BlockScope ifBlockScope;

    /**
     * Vykony kod v else - muze byt null
     */
    private final BlockScope elseBlockScope;

    /**
     * Konstruktor pro vytvoreni if statementu
     * @param depthLevel aktualni hloubka zanoreni
     * @param expression vyraz v ifu
     * @param ifBlockScope vykonny kod v ifu
     * @param elseBlockScope vykony kod v else - muze byt null
     */
    public IfStatement(long depthLevel, Expression expression, BlockScope ifBlockScope, BlockScope elseBlockScope) {
        super(StatementType.IfStatement, depthLevel);
        this.expression = expression;
        this.ifBlockScope = ifBlockScope;
        this.elseBlockScope = elseBlockScope;
    }

}
