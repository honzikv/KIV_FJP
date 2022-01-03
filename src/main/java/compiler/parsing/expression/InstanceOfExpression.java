package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;

public class InstanceOfExpression extends Expression {

    @Getter
    private final String identifier;

    @Getter
    private final DataType rightSide;

    public InstanceOfExpression(String identifier, DataType rightSide) {
        super(ExpressionType.InstanceOf);
        this.identifier = identifier;
        this.rightSide = rightSide;
    }
}
