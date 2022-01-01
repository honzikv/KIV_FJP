package compiler.parsing.statement.variable;

import compiler.parsing.DataType;
import compiler.parsing.expression.Expression;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class VariableInitializationStatement extends Statement {

    /**
     * Identifikator
     */
    private final String identifier;

    private final boolean isLiteralValue;

    /**
     * Prirazeni hodnotou
     */
    private final String literalValue;

    /**
     * Prirazeni vyrazem
     */
    private final Expression expression;

    /**
     * Datovy typ promenne
     */
    private final DataType dataType;


    /**
     * Zda-li je prirazeni konstanta
     */
    private final boolean isConst;


    public VariableInitializationStatement(long depthLevel,
                                           DataType dataType,
                                           String identifier,
                                           String literalValue,
                                           boolean isConst) {
        super(StatementType.VariableInitialization, depthLevel);
        this.identifier = identifier;
        this.literalValue = literalValue;
        this.dataType = dataType;
        this.isConst = isConst;
        this.isLiteralValue = true;
        this.expression = null;
    }


    public VariableInitializationStatement(long depthLevel,
                                           DataType dataType,
                                           String identifier,
                                           Expression expression,
                                           boolean isConst) {
        super(StatementType.VariableInitialization, depthLevel);
        this.dataType = dataType;
        this.isConst = isConst;
        this.identifier = identifier;
        this.expression = expression;
        this.isLiteralValue = false;
        this.literalValue = null;
    }


}
