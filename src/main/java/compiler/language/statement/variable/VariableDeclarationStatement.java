package compiler.language.statement.variable;

import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import compiler.language.variable.DataType;
import lombok.Getter;

@Getter
public class VariableDeclarationStatement extends Statement {

    private final String identifier;

    private final DataType dataType;

    public VariableDeclarationStatement(long depthLevel, String identifier, DataType dataType) {
        super(StatementType.VariableDeclaration, depthLevel);
        this.identifier = identifier;
        this.dataType = dataType;
    }
}
