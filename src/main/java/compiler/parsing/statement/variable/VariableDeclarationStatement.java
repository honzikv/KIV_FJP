package compiler.parsing.statement.variable;

import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import compiler.parsing.DataType;
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
