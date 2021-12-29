package compiler.runtime;

import compiler.parsing.DataType;
import compiler.parsing.expression.Expression;
import lombok.Getter;
import lombok.Setter;

/**
 * Reprezentuje promennou
 */
@Getter
@Setter
public class Variable {

    /**
     * Jmeno promenne
     */
    private String identifier;

    /**
     * Typ promenne
     */
    private DataType dataType;

    private String literalValue;

    /**
     * Velikost v blocich
     */
    private int sizeOf;

    /**
     * Zda-li je promenna inicializovana
     */
    private boolean isInitalized;

    /**
     * Zda-li je promenna const
     */
    private boolean isConstant;

    private boolean isLiteralExpression;

    private Expression expression;
}
