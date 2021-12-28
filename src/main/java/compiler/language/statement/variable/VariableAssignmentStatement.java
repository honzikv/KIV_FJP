package compiler.language.statement.variable;

import compiler.language.expression.Expression;
import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Prirazeni hodnoty nebo vyrazu promenne - toto dedi VariableInitialization, ktere jenom
 * navic promennou i rovnou vytvori
 */
@Getter
@ToString
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
    protected final boolean isLiteralExpression;

    /**
     * Prirazeni primo
     */
    protected final String literalValue;

    /**
     * Prirazeni vyrazem
     */
    protected final Expression expressionValue;


    public VariableAssignmentStatement(long depthLevel,
                                       String identifier,
                                       List<String> chainedIdentifiers,
                                       Expression expressionValue) {
        super(StatementType.VariableAssignment, depthLevel);
        this.identifier = identifier;
        this.chainedIdentifiers = chainedIdentifiers;
        this.isLiteralExpression = false;
        this.literalValue = null;
        this.expressionValue = expressionValue;
    }

    protected VariableAssignmentStatement(StatementType statementType,
                                          long depthLevel,
                                          String identifier,
                                          List<String> chainedIdentifiers,
                                          Expression expressionValue) {
        super(statementType, depthLevel);
        this.identifier = identifier;
        this.chainedIdentifiers = chainedIdentifiers;
        this.isLiteralExpression = false;
        this.literalValue = null;
        this.expressionValue = expressionValue;
    }

    public VariableAssignmentStatement(long depthLevel,
                                       String identifier,
                                       List<String> chainedIdentifiers,
                                       String literalValue) {
        super(StatementType.VariableInitialization, depthLevel);
        this.identifier = identifier;
        this.literalValue = literalValue;
        this.expressionValue = null;
        this.isLiteralExpression = true;
        this.chainedIdentifiers = chainedIdentifiers;
    }

    protected VariableAssignmentStatement(StatementType statementType,
                                          long depthLevel,
                                          String identifier,
                                          List<String> chainedIdentifiers,
                                          String literalValue) {
        super(statementType, depthLevel);
        this.identifier = identifier;
        this.literalValue = literalValue;
        this.expressionValue = null;
        this.isLiteralExpression = true;
        this.chainedIdentifiers = chainedIdentifiers;
    }
}