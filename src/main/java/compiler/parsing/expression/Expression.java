package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public abstract class Expression {

    @Getter
    @Setter
    protected DataType dataType;

    @Getter
    protected ExpressionType expressionType;

    protected Expression(DataType dataType) {
        this.dataType = dataType;
    }

    protected Expression() {
        dataType = null;
    }


}
