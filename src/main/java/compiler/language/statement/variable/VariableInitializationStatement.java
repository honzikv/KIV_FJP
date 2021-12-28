package compiler.language.statement.variable;

import compiler.language.expression.Expression;
import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import compiler.language.variable.DataType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Inicializace promenne - tzn. napr. string str = "hello world";
 * Trida zaroven uklada info i pro konstanty - const string str = "hello world";
 *
 */
@Getter
@ToString
public class VariableInitializationStatement extends Statement {

    /**
     * Datovy typ promenne
     */
    private final DataType dataType;

    /**
     * Identifikator promenne
     */
    private final String identifier;

    /**
     * Seznam zretezenych identifikatoru - muze byt prazdny
     */
    private final List<String> chainedIdentifiers;

    /**
     * Zda-li se jedna o prirazeni primo a nebo vyrazem
     */
    private final boolean isLiteralExpression;

    /**
     * Prirazeni primo
     */
    private final String literalValue;

    /**
     * Prirazeni vyrazem
     */
    private final Expression expressionValue;

    /**
     * Zda-li je prirazeni konstanta
     */
    private final boolean isConst;


    public VariableInitializationStatement(long depthLevel,
                                           DataType dataType,
                                           String identifier,
                                           List<String> chainedIdentifiers,
                                           String literalValue,
                                           boolean isConst) {
        super(StatementType.VariableInitialization, depthLevel);
        this.dataType = dataType;
        this.identifier = identifier;
        this.literalValue = literalValue;
        this.isLiteralExpression = true;
        this.expressionValue = null;
        this.isConst = isConst;
        this.chainedIdentifiers = chainedIdentifiers;
    }

    protected VariableInitializationStatement(StatementType statementType,
                                              long depthLevel,
                                              DataType dataType,
                                              String identifier,
                                              List<String> chainedIdentifiers,
                                              String literalValue,
                                              boolean isConst) {
        super(statementType, depthLevel);
        this.dataType = dataType;
        this.identifier = identifier;
        this.literalValue = literalValue;
        this.isLiteralExpression = true;
        this.expressionValue = null;
        this.isConst = isConst;
        this.chainedIdentifiers = chainedIdentifiers;
    }

    public VariableInitializationStatement(long depthLevel,
                                           DataType dataType,
                                           String identifier,
                                           List<String> chainedIdentifiers,
                                           Expression expressionValue,
                                           boolean isConst) {
        super(StatementType.VariableInitialization, depthLevel);
        this.dataType = dataType;
        this.identifier = identifier;
        this.literalValue = null;
        this.expressionValue = expressionValue;
        this.isLiteralExpression = false;
        this.isConst = isConst;
        this.chainedIdentifiers = chainedIdentifiers;
    }

    protected VariableInitializationStatement(StatementType statementType,
                                              long depthLevel,
                                              DataType dataType,
                                              String identifier,
                                              List<String> chainedIdentifiers,
                                              Expression expressionValue,
                                              boolean isConst) {
        super(statementType, depthLevel);
        this.dataType = dataType;
        this.identifier = identifier;
        this.literalValue = null;
        this.expressionValue = expressionValue;
        this.isLiteralExpression = false;
        this.isConst = isConst;
        this.chainedIdentifiers = chainedIdentifiers;
    }

}
