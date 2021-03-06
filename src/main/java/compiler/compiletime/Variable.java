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
     * Adresa
     */
    private long address;

    /**
     * Typ promenne
     */
    private DataType dataType;

    /**
     * Zda-li je promenna inicializovana
     */
    private boolean isInitialized = false;

    /**
     * Zda-li se promenna v kodu deklarovala
     */
    private boolean isDeclared = false;

    /**
     * Zda-li je promenna const
     */
    private boolean isConst = false;


    /**
     * Konstruktor, ktery slouzi pro vytvoreni neinicializovane a nedeklarovane promenne
     */
    public Variable(String identifier, long address, DataType dataType) {
        this.identifier = identifier;
        this.address = address;
        this.dataType = dataType;
    }


}
