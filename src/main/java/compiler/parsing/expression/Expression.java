package compiler.parsing.expression;

import compiler.compiletime.IResolvable;
import compiler.parsing.DataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public abstract class Expression implements IResolvable {

    @Getter
    @Setter
    protected DataType expectedOutput;

    protected Expression(DataType expectedOutput) { this.expectedOutput = expectedOutput; }

    protected Expression() {
        expectedOutput = null;
    }


}
