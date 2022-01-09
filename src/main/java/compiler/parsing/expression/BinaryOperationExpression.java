package compiler.parsing.expression;

import lombok.Getter;
import lombok.ToString;

/**
 * Reprezentuje binarni vyraz
 */
@Getter
@ToString
public class BinaryOperationExpression extends Expression {

    private final Expression leftSide;

    private final Expression rightSide;

    /**
     * Typ operace - scitani, odcitani, apod.
     */
    private final OperationType operation;

    public BinaryOperationExpression(Expression leftSide, Expression rightSide,
                                     OperationType operation) {
        super(ExpressionType.Binary);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operation = operation;
    }


}
