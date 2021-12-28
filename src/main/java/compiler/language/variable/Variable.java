package compiler.language.variable;

import compiler.language.expression.Expression;
import lombok.Getter;
import lombok.Setter;

/**
 * Reprezentuje promennou
 */
@Getter
@Setter
public class Variable<T> {

    /**
     * Jmeno promenne
     */
    private String name;

    /**
     * Typ promenne
     */
    private DataType dataType;

    private T literalValue;

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
