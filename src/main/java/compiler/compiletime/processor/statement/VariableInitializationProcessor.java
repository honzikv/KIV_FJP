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
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
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

    /**
     * Ziska vsechny identifikatory, ktere se prirazuji
     *
     * @return
     */
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

        processExpression(context, variables);
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
            throw new CompileException("Error, trying to assign" + dataType.getStringValue() +
                    " value to non-integer variable with" +
                    " identifier" + variable.getIdentifier());
        }

        // Pokud je promenna konstantni a inicializovana vyhodime exception
        // Neinicializovana je pouze v pripade, ze jsme ji prave vytvorili
        if (variable.isConst() && variable.isInitalized()) {
            throw new CompileException("Error, trying to reassign constant value!");
        }
    }

    /**
     * Inicializuje promennou, pokud neni inicializovana
     *
     * @param context
     * @param variable
     */
    private void initializeVariableIfUninitialized(GeneratorContext context, Variable variable) throws CompileException {
        if (variable.isInitalized()) {
            return;
        }

        switch (variable.getDataType()) {
            case Int -> context.allocateVariable(variable, 0);
            case Float -> context.allocateVariable(variable, 0.0f);
            case Boolean -> context.allocateVariable(variable, false);
            default -> throw new CompileException("Error, invalid data type for variable initialization provided");
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
            throw new CompileException("Error, invalid value for bool variable: " + value);
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
        variables.forEach(variable -> variable.setInitalized(true));
    }


    /**
     * Zpracuje vyraz a ulozi ho do seznamu promennych
     *
     * @param context   kontext, kam se vyraz bude ukladat
     * @param variables seznam promennych
     */
    private void processExpression(GeneratorContext context, List<Variable> variables) throws CompileException {
        var expression = variableStatement.getExpression();

        // Vyraz musime pridat na zasobnik
        var expressionProcessor = new ExpressionProcessor(expression);
        expressionProcessor.process(context);

        // Pro prvni promennou nacteme vysledek operace ze stacku
        var variable = variables.get(0);
        initializeVariableIfUninitialized(context, variable);
        validateVariable(variable, expression.getDataType());

        switch (variable.getDataType()) {
            case Int -> {
                if (!variable.isInitalized())
                    IntegerLib.loadToVariable(context, variable.getAddress());
            }
            case Boolean -> BooleanLib.loadToVariable(context, variable.getAddress());
            case Float -> FloatLib.loadToVariable(context, variable.getAddress());
            default -> throw new CompileException("Error while reading variable from stack during expression assignment");
        }
        variable.setInitalized(true);

        // Pro zbytek nacteme vysledek z promenne na stack a ze stacku na jejich adresu
        for (var i = 1; i < variables.size(); i += 1) {
            var chainedVariable = variables.get(i);
            validateVariable(chainedVariable, expression.getDataType());
            initializeVariableIfUninitialized(context, chainedVariable);

            switch (variable.getDataType()) {
                case Int -> {
                    IntegerLib.loadFromVariable(context, variable.getAddress());
                    IntegerLib.loadToVariable(context, chainedVariable.getAddress());
                }
                case Boolean -> {
                    BooleanLib.loadFromVariable(context, variable.getAddress());
                    BooleanLib.loadToVariable(context, chainedVariable.getAddress());
                }
                case Float -> {
                    FloatLib.loadFromVariable(context, variable.getAddress());
                    FloatLib.loadToVariable(context, chainedVariable.getAddress());
                }
                default -> throw new CompileException("Error while reading " +
                        "variable from stack during expression assignment");
            }
            chainedVariable.setInitalized(true);
        }
    }
}
