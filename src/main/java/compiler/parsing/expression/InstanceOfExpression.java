package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;

/**
 * Vyraz instanceof
 */
public class InstanceOfExpression extends Expression {

    @Getter
    private final String identifier; // muze byt i datovy typ int / bool

    @Getter
    private final DataType rightSide;

    public InstanceOfExpression(String identifier, DataType rightSide) {
        super(ExpressionType.InstanceOf);
        this.identifier = identifier;
        this.rightSide = rightSide;
    }
}
