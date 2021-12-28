package compiler.language.expression;

import compiler.language.variable.DataType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BinaryOperationExpression extends Expression{

    private final Expression leftSide;

    private final Expression rightSide;

    private OperationType operation;

    public BinaryOperationExpression(DataType expectedOutput, Expression leftSide, Expression rightSide) {
        super(expectedOutput);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }
}
