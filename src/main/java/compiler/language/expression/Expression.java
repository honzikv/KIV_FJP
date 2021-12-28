package compiler.language.expression;

import compiler.language.variable.DataType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Expression {

    private final DataType expectedOutput;

    protected Expression(DataType expectedOutput) { this.expectedOutput = expectedOutput; }
}
