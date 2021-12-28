package compiler.language.statement.function;

import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import compiler.language.variable.DataType;
import lombok.Getter;

@Getter
public class FunctionParameter extends Statement {

    private final DataType dataType;

    private final String identifier;

    public FunctionParameter(long depthLevel, DataType dataType, String identifier) {
        super(StatementType.FunctionParameter, depthLevel);
        this.dataType = dataType;
        this.identifier = identifier;
    }
}
