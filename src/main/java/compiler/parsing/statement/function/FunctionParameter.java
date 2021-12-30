package compiler.parsing.statement.function;


import compiler.parsing.DataType;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FunctionParameter extends Statement {

    private final DataType dataType;

    private final String identifier;

    public FunctionParameter(long depthLevel, DataType dataType, String identifier) {
        super(StatementType.FunctionParameter, depthLevel);
        this.dataType = dataType;
        this.identifier = identifier;
    }
}
