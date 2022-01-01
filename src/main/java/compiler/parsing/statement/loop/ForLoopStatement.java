package compiler.parsing.statement.loop;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import lombok.Getter;
import lombok.ToString;

/**
 * Reprezentuje for cyklus
 */
@ToString
@Getter
public class ForLoopStatement extends Statement {

    private final Expression start;

    private final Expression end;

    private final BlockScope blockScope;

    /**
     * Identifikator k iteracni promenne
     */
    private final String identifier;

    /**
     * Konstruktor pro for cyklus
     *
     * @param depth      hloubka zanoreni
     * @param start      zacatek
     * @param end        konec
     * @param blockScope scope s vykonnym kodem
     */
    public ForLoopStatement(long depth, Expression start, Expression end, BlockScope blockScope, String identifier) {
        super(StatementType.ForLoop, depth);
        this.start = start;
        this.end = end;
        this.blockScope = blockScope;
        this.identifier = identifier;
    }

}
