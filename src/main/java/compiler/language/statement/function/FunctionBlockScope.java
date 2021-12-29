package compiler.language.statement.function;

import compiler.language.statement.BlockScope;
import lombok.Getter;

@Getter
public class FunctionBlockScope extends BlockScope {

    private final ReturnStatement returnStatement;

    public FunctionBlockScope(long depthLevel, ReturnStatement returnStatement) {
        super(depthLevel);
        this.returnStatement = returnStatement;
    }

}
