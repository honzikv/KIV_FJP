package compiler.parsing.expression;

import compiler.parsing.statement.function.FunctionCall;
import lombok.Getter;

/**
 * Vyraz volani funkce
 */
public class FunctionCallExpression extends Expression {

    @Getter
    private final FunctionCall functionCall;

    public FunctionCallExpression(FunctionCall functionCall) {
        super(ExpressionType.FunctionCall);
        this.functionCall = functionCall;
    }
}
