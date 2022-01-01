package compiler.compiletime.processor.statement.variable;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VariableDeclarationProcessor implements IProcessor {

    private final VariableDeclarationStatement variableDeclarationStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var identifier = variableDeclarationStatement.getIdentifier();
        if (context.variableDeclaredInCurrentScope(identifier)) {
            throw new CompileException("Error, variable with identifier: " + identifier
                    + " was redeclared in the same scope!");
        }

        context.declareVariable(identifier);
    }
}
