package compiler.compiletime.processor.statement;

import compiler.compiletime.DataTypeParseUtils;
import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.Variable;
import compiler.compiletime.libs.BooleanLib;
import compiler.compiletime.libs.FloatLib;
import compiler.compiletime.libs.IntegerLib;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.parsing.DataType;
import compiler.parsing.expression.Expression;
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Prida promenne do kontextu. Pokud v kontextu uz existuji vyhodi vyjimky
     * Tato metoda se musi volat pouze pro statement, ktery je VariableInitializationStatement
     *
     * @param identifiers identifikator promenne + chained promenne
     */
    private void declareVariablesIfNotExist(GeneratorContext context, List<String> identifiers) throws CompileException {
        for (var identifier : identifiers) {
            if (context.variableExistsInCurrentScope(identifier)) {
                throw new CompileException("Error, trying to create a variable with an existing identifier "
                        + (((VariableInitializationStatement) variableStatement).getDataType().toString() + " " +
                        identifier));
            }

            // Pridame promennou jako deklaraci
            var variable = Variable.createVariableDeclaration(variableStatement.getIdentifier(),
                    ((VariableInitializationStatement) variableStatement).getDataType());

            // Pokud je modifier const nastavime promennou jako const
            if (((VariableInitializationStatement) variableStatement).isConst()) {
                variable.setConst(true);
                variable.setInitalized(false);
            }

            context.addVariableToLookupTable(variable);
        }
    }

    private List<String> getVariableIdentifiers() {
        var identifiers = new ArrayList<String>();
        identifiers.add(variableStatement.getIdentifier());
        identifiers.addAll(variableStatement.getChainedIdentifiers());
        return identifiers;
    }

    // TODO - stringy - nesmeji se pridavat pres operator +, jinak je slozite zjistovat odkud string nacist
    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Protoze VariableInitializationStatement extenduje VariableAssignmentStatement muzeme zkusit, jestli
        // je field instanci inicializace a pokud ano vytvorime novou promennou
        if (variableStatement instanceof VariableInitializationStatement) {
            declareVariablesIfNotExist(context, getVariableIdentifiers());
        }

        var identifiers = getVariableIdentifiers();
        var variables = new ArrayList<Variable>();
        // Projedeme identifikatory a ziskame promenne
        for (var identifier : identifiers) {
            var variable = context.getVariableOrDefault(identifier);
            if (variable == null) {
                throw new CompileException("Error, variable with identifier " + identifier + " was assigned before declaration");
            }

            variables.add(variable);
        }

        // Zpracujeme v zavislosti na tom, co za typ vyrazu to je
        if (variableStatement.isLiteralExpression()) {
            processLiteralExpression(context, variables);
            return;
        }

        processExpression(variables);
    }

    /**
     * Provede validaci promenne, zda-li do ni lze vubec dana data priradit
     *
     * @param variable
     * @param dataType
     * @throws CompileException
     */
    private void validateVariable(Variable variable, DataType dataType) throws CompileException {
        if (variable.getDataType() != dataType) {
            throw new CompileException("Error, trying to assign integer value to non-integer variable with" +
                    " identifier" + variable.getIdentifier());
        }

        // Pokud je promenna konstantni a inicializovana vyhodime exception
        // Neinicializovana je pouze v pripade, ze jsme ji prave vytvorili
        if (variable.isConst() && variable.isInitalized()) {
            throw new CompileException("Error, trying to reassign constant value!");
        }
    }

    private void processIntegerVariables(GeneratorContext context, List<Variable> variables, String value)
            throws CompileException {
        var intValue = DataTypeParseUtils.getIntegerOrDefault(value);
        if (intValue == null) {
            throw new CompileException("Error invalid value for integer variable: " + value);
        }

        for (var variable : variables) {
            validateVariable(variable, DataType.Int);
            if (!variable.isInitalized()) {
                context.allocateVariable(variable, intValue);
                continue;
            }

            // Jinak pridame hodnotu na stack a nacteme do dane adresy
            IntegerLib.addOnStack(context, intValue);
            IntegerLib.loadToVariable(context, variable.getAddress());
        }
    }

    private void processFloatVariables(GeneratorContext context, List<Variable> variables, String value)
            throws CompileException {
        var floatValue = DataTypeParseUtils.getFloatOrDefault(value);
        if (floatValue == null) {
            throw new CompileException("Error, invalid value for float variable: " + value);
        }

        for (var variable : variables) {
            validateVariable(variable, DataType.Float);
            if (!variable.isInitalized()) {
                context.allocateVariable(variable, floatValue);
                continue;
            }

            FloatLib.addOnStack(context, floatValue);
            FloatLib.loadToVariable(context, variable.getAddress());
        }
    }

    private void processBooleanVariables(GeneratorContext context, List<Variable> variables, String value)
            throws CompileException {
        var boolValue = DataTypeParseUtils.getBooleanOrDefault(value);
        if (boolValue == null) {
            throw new CompileException("Error, invalid value for float variable: " + value);
        }

        for (var variable : variables) {
            validateVariable(variable, DataType.Boolean);
            if (!variable.isInitalized()) {
                context.allocateVariable(variable, boolValue);
                continue;
            }

            BooleanLib.addOnStack(context, boolValue);
            BooleanLib.loadToVariable(context, variable.getAddress());
        }
    }

    private void processStringVariables(GeneratorContext context, List<Variable> variables, String value)
            throws CompileException {
        var stringValue = value == null ? "" : value;

        for (var variable : variables) {
            validateVariable(variable, DataType.String);

            // Zde je nejsnazsi zpusob pridat string na stack jednou a referencovat ho se vsemi promennymi
            throw new CompileException("TODO implement this");
        }
    }

    private void processLiteralExpression(GeneratorContext context, List<Variable> variables) throws CompileException {
        var dataType = variables.get(0).getDataType(); // ziskame datovy typ
        var value = variableStatement.getLiteralValue();

        switch (dataType) {
            case Int -> processIntegerVariables(context, variables, value);
            case Float -> processFloatVariables(context, variables, value);
            case Boolean -> processBooleanVariables(context, variables, value);
            case String -> processStringVariables(context, variables, value);
            default -> throw new CompileException("Error, invalid data type present");
        }
    }

    private void processExpression(List<Variable> variables) {

    }
}
