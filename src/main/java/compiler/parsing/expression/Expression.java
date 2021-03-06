package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reprezentuje abstraktni vyraz
 */
@ToString
public abstract class Expression {

    @Getter
    @Setter
    protected DataType dataType;

    @Getter
    protected ExpressionType expressionType;

    protected Expression(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    protected Expression(ExpressionType expressionType, DataType dataType) {
        this.expressionType = expressionType;
        this.dataType = dataType;
    }


}
