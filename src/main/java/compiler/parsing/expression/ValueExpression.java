package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;
import lombok.ToString;

/**
 * Vyraz, ktery je automaticky vyhodnotitelny - tzn. cislo nebo boolean
 */
@Getter
@ToString
public class ValueExpression extends Expression {

    private final String value;

    public ValueExpression(DataType dataType, String value) {
        // Ocekavany vystup je samozrejme to, co vstoupilo
        super(ExpressionType.Value, dataType);
        this.expressionType = ExpressionType.Value;
        this.value = value;
    }

}
