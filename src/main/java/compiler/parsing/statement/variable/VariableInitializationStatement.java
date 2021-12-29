package compiler.parsing.statement.variable;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.StatementType;
import compiler.parsing.DataType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Inicializace promenne - tzn. napr. string str = "hello world";
 * Trida zaroven uklada info i pro konstanty - const string str = "hello world";
 *
 */
@Getter
@ToString(callSuper = true)
public class VariableInitializationStatement extends VariableAssignmentStatement {

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
                                           List<String> chainedIdentifiers,
                                           String literalValue,
                                           boolean isConst) {
        super(StatementType.VariableInitialization, depthLevel, identifier, chainedIdentifiers,
                literalValue);
        this.dataType = dataType;
        this.isConst = isConst;
    }


    public VariableInitializationStatement(long depthLevel,
                                           DataType dataType,
                                           String identifier,
                                           List<String> chainedIdentifiers,
                                           Expression expressionValue,
                                           boolean isConst) {
        super(StatementType.VariableInitialization, depthLevel, identifier, chainedIdentifiers,
                expressionValue);
        this.dataType = dataType;
        this.isConst = isConst;
    }


}
