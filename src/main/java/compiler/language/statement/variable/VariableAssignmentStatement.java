package compiler.language.statement.variable;

import compiler.language.expression.Expression;
import compiler.language.statement.StatementType;
import compiler.language.variable.DataType;
import java.util.List;

public class VariableAssignmentStatement extends VariableInitializationStatement {

    public VariableAssignmentStatement(long depthLevel, DataType dataType, String identifier,
                                       List<String> chainedIdentifiers, String literalValue) {
        super(StatementType.VariableAssignment, depthLevel, dataType, identifier, chainedIdentifiers,
                literalValue, false);
    }

    public VariableAssignmentStatement(long depthLevel, DataType dataType, String identifier,
                                       List<String> chainedIdentifiers,
                                       Expression expressionValue) {
        super(StatementType.VariableAssignment, depthLevel, dataType, identifier,
                chainedIdentifiers,
                expressionValue, false);
    }
}
