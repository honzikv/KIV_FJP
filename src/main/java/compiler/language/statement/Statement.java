package compiler.language.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Statement - abstraktni trida
 */
@AllArgsConstructor
public abstract class Statement {

    /**
     * Typ statementu
     */
    @Getter
    private final StatementType statementType;

    @Getter
    private final long depthLevel;
}
