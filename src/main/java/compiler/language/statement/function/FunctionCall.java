package compiler.language.statement.function;

import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import java.util.List;
import lombok.Getter;

@Getter
public class FunctionCall extends Statement {

    private final String identifier;

    private final List<String> paramIdentifiers;

    public FunctionCall(long depthLevel, String identifier, List<String> paramIdentifiers) {
        super(StatementType.FunctionCall, depthLevel);
        this.identifier = identifier;
        this.paramIdentifiers = paramIdentifiers;
    }
}
