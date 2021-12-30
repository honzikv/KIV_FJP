package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.Variable;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.parsing.expression.Expression;
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

/**
 * Trida slouzi jak pro inicializaci, tak i pro assignment, protoze funkcionalita je velmi podobna
 */
@AllArgsConstructor
public class VariableInitializationProcessor implements IProcessor {

    private final VariableAssignmentStatement variableStatement;

    private Variable getVariableOrThrowException(GeneratorContext context, String identifier) throws CompileException {
        var variable = context.getVariableOrDefault(identifier);
        if (variable == null) {
            throw new CompileException("Error, variable with identifier " + variableStatement.getIdentifier() +
                    " does not exist!");
        }
        return variable;
    }

    /**
     * Nastavi hodnotu dane promenne
     *
     * @param value      hodnota
     * @param identifier promenna
     */
    private void setVariableValue(GeneratorContext context, String value, String identifier) throws CompileException {
        // Ziskame promennou
        var variable = getVariableOrThrowException(context, identifier);
    }

    /**
     * Nastavi vysledek vyrazu pro danou promennou
     *
     * @param context
     * @param expression
     * @param identifier
     */
    private void setVariableExpression(GeneratorContext context, Expression expression, String identifier)
            throws CompileException {
        var variable = getVariableOrThrowException(context, identifier);

        // Vyraz musime vyhodnotit
        var expressionProcessor = new ExpressionProcessor(expression);
        expressionProcessor.process(context);

        // Nyni je na stacku vysledek vyrazu, takze ho muzeme nacist do promenne
        if (variable.isInitalized()) {
            // Promenna je inicializovana, takze staci nahrat do pameti nekde na stacku
            context.addInstruction(PL0InstructionType.LOD, variable.getStackLevel(), variable.getAddress());
        }
    }

    void createVariableIfNotInitialized(GeneratorContext context, Variable variable) {
        if (variable.isInitalized()) {
            return;
        }

        context.addInstruction(PL0InstructionType.LIT, 0, 0);
        variable.setAddress(context.getStackPointerAddress());
    }


    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Protoze VariableInitializationStatement extenduje VariableAssignmentStatement muzeme zkusit, jestli
        // je field instanci inicializace a pokud ano vytvorime novou promennou
        if (variableStatement instanceof VariableInitializationStatement) {
            if (context.variableExistsInCurrentScope(variableStatement.getIdentifier())) {
                throw new CompileException("Error, trying to create a variable with an existing identifier "
                        + (((VariableInitializationStatement) variableStatement).getDataType().toString() + " " +
                        variableStatement.getIdentifier()));
            }

            // Pridame promennou jako deklaraci
            var variable = Variable.createVariableDeclaration(variableStatement.getIdentifier(),
                    ((VariableInitializationStatement) variableStatement).getDataType());
            context.addVariableToLookupTable(variable);
        }

        var variable = getVariableOrThrowException(context, variableStatement.getIdentifier());

        for (var chainedVariable : variableStatement.getChainedIdentifiers()) {

        }
    }
}
