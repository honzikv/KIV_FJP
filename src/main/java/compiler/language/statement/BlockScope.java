package compiler.language.statement;


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Reprezentuje "{ statement* }"
 */
public class BlockScope extends Statement {

    /**
     * Vsechny statementy v bloku
     */
    @Getter
    private final List<Statement> childStatements = new ArrayList<>();

    /**
     * Konstruktor
     * @param depthLevel hloubka zanoreni
     */
    public BlockScope(long depthLevel) {
        super(StatementType.BlockStatement, depthLevel);
    }

    /**
     * Prida statement na konec seznamu
     * @param statement statement, ktery se ma pridat
     */
    public void addStatement(Statement statement) {
        childStatements.add(statement);
    }
}
