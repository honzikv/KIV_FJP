package compiler.parsing.expression;

import compiler.parsing.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public abstract class Expression {

    @Getter
    @Setter
    protected DataType expectedOutput;

    protected Expression(DataType expectedOutput) { this.expectedOutput = expectedOutput; }

    protected Expression() {
        expectedOutput = null;
    }
}
