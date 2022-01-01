package compiler.parsing.statement.variable;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Prirazeni hodnoty nebo vyrazu promenne - toto dedi VariableInitialization, ktere jenom
 * navic promennou i rovnou vytvori
 */
@Getter
@ToString(callSuper = true)
public class VariableAssignmentStatement extends Statement {

    /**
     * Identifikator promenne
     */
    protected final String identifier;

    /**
     * Seznam zretezenych identifikatoru - muze byt prazdny
     */
    protected final List<String> chainedIdentifiers;

    /**
     * Zda-li se jedna o prirazeni primo a nebo vyrazem
     */
    protected final boolean isLiteralValue;

    /**
     * Prirazeni primo
     */
    protected final String literalValue;

    /**
     * Prirazeni vyrazem
     */
    protected final Expression expression;


    public VariableAssignmentStatement(long depthLevel,
                                       String identifier,
                                       List<String> chainedIdentifiers,
                                       Expression expression) {
        super(StatementType.VariableAssignment, depthLevel);
        this.identifier = identifier;
        this.chainedIdentifiers = chainedIdentifiers;
        this.isLiteralValue = false;
        this.literalValue = null;
        this.expression = expression;
    }

    public VariableAssignmentStatement(long depthLevel,
                                       String identifier,
                                       List<String> chainedIdentifiers,
                                       String literalValue) {
        super(StatementType.VariableAssignment, depthLevel);
        this.identifier = identifier;
        this.literalValue = literalValue;
        this.expression = null;
        this.isLiteralValue = true;
        this.chainedIdentifiers = chainedIdentifiers;
    }

}
