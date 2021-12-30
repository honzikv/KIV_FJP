package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.Variable;
import compiler.parsing.DataType;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VariableDeclarationProcessor implements IProcessor {

    private final VariableDeclarationStatement variableDeclarationStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var identifier = variableDeclarationStatement.getIdentifier();
        if (context.variableExistsInCurrentScope(identifier)) {
            throw new CompileException("Error, variable with identifier: " + identifier + " was redeclared in the same scope!");
        }

        if (variableDeclarationStatement.getDataType() == DataType.String) {
            throw new CompileException("Error, variable with type string cannot be declared!");
        }

        // Pridame deklaraci do kontextu
        context.addVariableToLookupTable(Variable.createVariableDeclaration(variableDeclarationStatement.getIdentifier(),
                variableDeclarationStatement.getDataType()));
    }
}
