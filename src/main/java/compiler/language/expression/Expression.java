package compiler.language.expression;

import compiler.language.variable.DataType;
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
