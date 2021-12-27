package compiler.language.variable;

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
    private String name;

    /**
     * Typ promenne
     */
    private DataType dataType;

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
}
