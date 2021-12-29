package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;
import lombok.ToString;

/**
 * Vyraz, ktery je automaticky vyhodnotitelny - tzn. cislo nebo boolean
 */
@Getter
@ToString
public class ValueExpression extends Expression{

    private final DataType dataType;

    private final String value;

    public ValueExpression(DataType dataType, String value) {
        super(dataType); // ocekavany vystup je samozrejme datovy typ toho co vstoupilo
        this.dataType = dataType;
        this.value = value;
    }


}
