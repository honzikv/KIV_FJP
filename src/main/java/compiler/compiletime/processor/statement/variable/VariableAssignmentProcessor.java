package compiler.compiletime.processor.statement.variable;

import compiler.compiletime.DataTypeParseUtils;
import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.Variable;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
import compiler.compiletime.utils.VariableUtils;
import compiler.parsing.DataType;
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.utils.CompileException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * Zpracovavani prirazeni k promennym
 */
@AllArgsConstructor
public class VariableAssignmentProcessor implements IProcessor {

    private VariableAssignmentStatement variableAssignmentStatement;

    /**
     * Provede validaci promenne, zda-li do ni lze vubec dana data priradit
     *
     * @param variable
     * @param dataType
     * @throws CompileException
     */
    private static void validateVariable(Variable variable, DataType dataType) throws CompileException {
        if (variable.getDataType() != dataType) {
            throw new CompileException("Error, trying to assign" + dataType.getStringValue() +
                    " value to non-integer variable with" +
                    " identifier" + variable.getIdentifier());
        }

        if (!variable.isDeclared()) {
            throw new CompileException("Error, trying to assign variable before declaration!");
        }

        // Pokud je promenna konstantni a inicializovana vyhodime exception
        // Neinicializovana je pouze v pripade, ze jsme ji prave vytvorili
        if (variable.isConst() && variable.isInitialized()) {
            throw new CompileException("Error, trying to reassign constant variable!");
        }
    }

    private static void processIntegerVariable(GeneratorContext context, Variable variable, String value)
            throws CompileException {
        var intValue = DataTypeParseUtils.getIntegerOrDefault(value);
        if (intValue == null) {
            throw new CompileException("Error invalid value for integer variable: " + value);
        }
        // Validujeme zda-li je typ platny a nastavime hodnotu
        validateVariable(variable, DataType.Int);
        IntegerUtils.addOnStack(context, intValue);
        IntegerUtils.storeToStackAddress(context, variable.getAddress());
    }

    // Tyto funkce by asi sli udelat chytreji genericky

    private static void processFloatVariable(GeneratorContext context, Variable variable, String value)
            throws CompileException {
        var floatValue = DataTypeParseUtils.getFloatOrDefault(value);
        if (floatValue == null) {
            throw new CompileException("Error, invalid value for float variable: " + value);
        }

        validateVariable(variable, DataType.Float);
        FloatUtils.addOnStack(context, floatValue);
        FloatUtils.storeToStackAddress(context, variable.getAddress());
    }

    private static void processBooleanVariable(GeneratorContext context, Variable variable, String value)
            throws CompileException {
        var boolValue = DataTypeParseUtils.getBooleanOrDefault(value);
        if (boolValue == null) {
            throw new CompileException("Error, invalid value for bool variable: " + value);
        }

        validateVariable(variable, DataType.Boolean);
        BooleanUtils.addOnStack(context, boolValue);
        BooleanUtils.storeToStackAddress(context, variable.getAddress());
    }

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var identifiers = new ArrayList<String>();
        identifiers.add(variableAssignmentStatement.getIdentifier());
        identifiers.addAll(variableAssignmentStatement.getChainedIdentifiers());

        var variables = new ArrayList<Variable>();

        // Ziskame promenne
        for (var identifier : identifiers) {
            var variable = context.getVariableOrDefault(identifier);
            // Tento if slouzi pro debug - pri normalnim pouzivani by mela byt promena vzdy v kontextu
            if (variable == null) {
                throw new CompileException("Error, variable with identifier "
                        + variable.getIdentifier() + " does not exist");
            }
            variables.add(variable);
        }

        // Zkontrolujeme, zda-li se jedna o prirazeni vyrazem nebo statickou hodnotou
        if (variableAssignmentStatement.isLiteralValue()) {
            processLiteralValueAssignment(context, variables);
            return;
        }

        // Prirazeni vyrazem
        processExpression(context, variables);
    }

    private void processLiteralValueAssignment(GeneratorContext context, List<Variable> variables) throws CompileException {
        var value = variableAssignmentStatement.getLiteralValue();

        for (var variable : variables) {
            switch (variable.getDataType()) {
                case Int -> VariableAssignmentProcessor.processIntegerVariable(context, variable, value);
                case Float -> VariableAssignmentProcessor.processFloatVariable(context, variable, value);
                case Boolean -> VariableAssignmentProcessor.processBooleanVariable(context, variable, value);
                default -> throw new CompileException("Error, invalid data type present");
            }
            variable.setInitialized(true);
        }
    }


    /**
     * Zpracuje vyraz a ulozi ho do seznamu promennych
     *
     * @param context   kontext, kam se vyraz bude ukladat
     * @param variables seznam promennych
     */
    private void processExpression(GeneratorContext context, List<Variable> variables) throws CompileException {
        var expression = variableAssignmentStatement.getExpression();

        // Vyraz musime pridat na zasobnik
        var expressionProcessor = new ExpressionProcessor(expression);
        expressionProcessor.process(context);

        // Pro prvni promennou nacteme vysledek operace ze stacku
        var variable = variables.get(0);
        VariableAssignmentProcessor.validateVariable(variable, expression.getDataType());

        // Nacteme dana data do promenne
        VariableUtils.storeToVariable(context, variable);
        variable.setInitialized(true);

        // Pro zbytek nacteme vysledek z promenne na stack a ze stacku na jejich adresu
        for (var i = 1; i < variables.size(); i += 1) {
            var chainedVariable = variables.get(i);
            VariableAssignmentProcessor.validateVariable(chainedVariable, expression.getDataType());
            VariableUtils.storeToVariable(context, variable, chainedVariable);
            chainedVariable.setInitialized(true);
        }
    }
}
