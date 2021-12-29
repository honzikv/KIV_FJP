package compiler.parsing.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Statement - abstraktni trida
 */
@AllArgsConstructor
@ToString
@Getter
public abstract class Statement {

    /**
     * Typ statementu
     */
    private final StatementType statementType;

    private final long depthLevel;

}
