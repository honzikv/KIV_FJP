package compiler.parsing.expression;

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
     * Vyraz za nebo pred operatorem
     */
    private final Expression childExpression;

    public UnaryOperationExpression(Expression childExpression, OperationType operation) {
        this.operation = operation;
        this.childExpression = childExpression;
    }
}
