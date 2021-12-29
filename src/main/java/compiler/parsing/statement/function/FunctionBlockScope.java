package compiler.parsing.statement.function;

import compiler.parsing.statement.BlockScope;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class FunctionBlockScope extends BlockScope {

    private final ReturnStatement returnStatement;

    public FunctionBlockScope(long depthLevel, ReturnStatement returnStatement) {
        super(depthLevel);
        this.returnStatement = returnStatement;
    }

}
