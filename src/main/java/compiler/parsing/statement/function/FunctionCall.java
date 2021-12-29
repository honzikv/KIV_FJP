package compiler.parsing.statement.function;

import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FunctionCall extends Statement {

    private final String identifier;

    private final List<String> paramIdentifiers;

    public FunctionCall(long depthLevel, String identifier, List<String> paramIdentifiers) {
        super(StatementType.FunctionCall, depthLevel);
        this.identifier = identifier;
        this.paramIdentifiers = paramIdentifiers;
    }
}
