package compiler.language.expression;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UnaryOperationExpression extends Expression {

    /**
     * Operator
     */
    private final OperationType operation;

    /**
     * Leva strana
     */
    private final Expression leftSide;

    public UnaryOperationExpression(Expression leftSide, OperationType operation) {
        this.operation = operation;
        this.leftSide = leftSide;
    }
}
