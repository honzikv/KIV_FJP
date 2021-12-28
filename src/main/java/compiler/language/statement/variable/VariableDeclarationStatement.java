package compiler.language.statement.variable;

import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import compiler.language.variable.DataType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VariableDeclarationStatement extends Statement {

    protected final String identifier;

    protected final DataType dataType;

    public VariableDeclarationStatement(long depthLevel, String identifier, DataType dataType) {
        super(StatementType.VariableDeclaration, depthLevel);
        this.identifier = identifier;
        this.dataType = dataType;
    }
}
