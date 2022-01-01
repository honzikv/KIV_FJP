package compiler.compiletime;

import compiler.parsing.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje promennou
 */
@Getter
@Setter
@ToString
public class Variable {

    /**
     * Jmeno promenne
     */
    private String identifier;

    /**
     * Uroven zanoreni v zasobniku
     */
    private Long stackLevel;

    /**
     * Adresa
     */
    private Long address;

    /**
     * Typ promenne
     */
    private DataType dataType;

    /**
     * Zda-li je promenna inicializovana
     */
    private boolean isInitalized = false;

    /**
     * Zda-li je promenna const
     */
    private boolean isConst = false;

    private Variable() { }

    public static Variable createVariableDeclaration(String identifier, DataType dataType) {
        var result = new Variable();
        result.identifier = identifier;
        result.isInitalized = false;
        result.address = null;
        result.stackLevel = null;
        result.dataType = dataType;
        return result;
    }

    public static Variable createVariableInitialization(String identifier, DataType dataType, long address,
                                                        long stackLevel, boolean isConst) {
        var result = new Variable();
        result.identifier = identifier;
        result.isInitalized = true;
        result.address = address;
        result.stackLevel = stackLevel;
        result.dataType = dataType;
        result.isConst = isConst;
        return result;
    }
}
