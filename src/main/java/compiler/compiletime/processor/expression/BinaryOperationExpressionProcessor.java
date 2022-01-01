package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.DataType;
import compiler.parsing.expression.BinaryOperationExpression;
import compiler.parsing.expression.Expression;
import compiler.parsing.expression.OperationType;
import compiler.parsing.expression.ValueExpression;
import compiler.pl0.PL0InstructionType;
import compiler.pl0.PL0Utils;
import compiler.utils.CompileException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

@AllArgsConstructor
public class BinaryOperationExpressionProcessor implements IProcessor {

    private final BinaryOperationExpression expression;

    /**
     * Podporovane typy operaci
     */
    private static final Map<DataType, Set<OperationType>> supportedOperations = createSupportedOperationsMap();

    /**
     * Operace, po kterych bude na vrcholu stacku booleovska hodnota
     */
    private static final Set<OperationType> booleanOperations = Set.of(
            OperationType.Equal,
            OperationType.GreaterThan,
            OperationType.GreaterOrEqual,
            OperationType.Lesser,
            OperationType.LesserOrEqual,
            OperationType.NotEqual);

    private static Map<DataType, Set<OperationType>> createSupportedOperationsMap() {
        var result = new HashMap<DataType, Set<OperationType>>();
        result.put(DataType.String, Set.of(OperationType.Equal, OperationType.NotEqual, OperationType.Addition));
        var supportedFloatOperations =
                Set.of(OperationType.Addition,
                        OperationType.Subtraction,
                        OperationType.Multiplication,
                        OperationType.Division,
                        OperationType.Modulo,
                        OperationType.Equal,
                        OperationType.GreaterThan,
                        OperationType.GreaterOrEqual,
                        OperationType.Lesser,
                        OperationType.LesserOrEqual,
                        OperationType.NotEqual);
        result.put(DataType.Float, supportedFloatOperations);
        result.put(DataType.Int, supportedFloatOperations);

        result.put(DataType.Boolean, Set.of(OperationType.BooleanAnd, OperationType.BooleanOr));

        return result;
    }


    private void validate(Expression leftSide, Expression rightSide) throws CompileException {
        if (leftSide.getDataType() != rightSide.getDataType()) {
            throw new CompileException("Error, different operand types present in binary expression. Left side is "
                    + leftSide.getDataType().getStringValue() + getValueIfValueExpression(leftSide)
                    + " while right side is " + rightSide.getDataType().getStringValue()
                    + getValueIfValueExpression(rightSide));
        }

        var dataType = leftSide.getDataType();
        if (!supportedOperations.get(dataType).contains(expression.getOperation())) {
            throw new CompileException("Error, operation " + expression.getOperation().getStringValue()
                    + " is not supported with type " + dataType.getStringValue());
        }
    }

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // K resolvovani potrebujeme aby se resolvovali oba dva vyrazy
        // Vytvorime obecny processor a nechame ho zpracovat levou i pravou stranu
        var expressionProcessor = new ExpressionProcessor();

        var leftSide = expression.getLeftSide();
        var rightSide = expression.getRightSide();
        // TODO zachova prioritu??? snad jo
        expressionProcessor.process(context, leftSide);
        expressionProcessor.process(context, rightSide);

        // processor nam automaticky na levou a pravou stranu nastavil datovy typ, takze resolvovani tohoto je
        // jednoduche. Provedeme check, jestli typy sedi a jestli pro ne operator existuje
        validate(leftSide, rightSide);

        if (expression.getLeftSide().getDataType() == DataType.Int) {
            processIntegerOperation(context);
            return;
        }

        if (expression.getLeftSide().getDataType() == DataType.Boolean) {
            processBooleanOperation(context);
            return;
        }

        // TODO
        processFloatOperation(context);
    }


    /**
     * Ulozi na zasobnik kod operace pro integer
     *
     * @param context kontext, kam se kod uklada
     * @throws CompileException vyhazuje metoda getOperationFromOperationType
     */
    private void processIntegerOperation(GeneratorContext context) throws CompileException {
        var operationCode = PL0Utils.getOperationNumberFromOperationType(expression.getOperation());
        context.addInstruction(PL0InstructionType.OPR, context.getStackLevel(), operationCode);

        // Pokud je operace porovnani (<, >, ==, !=, >=, <=, ...) bude vysledek typu boolean
        if (booleanOperations.contains(expression.getOperation())) {
            expression.setDataType(DataType.Boolean);
            return;
        }

        // Pro ostatni operace bude datovy typ int
        expression.setDataType(DataType.Int);
    }

    private void processBooleanOperation(GeneratorContext context) throws CompileException {
        var operationCode = PL0Utils.getOperationNumberFromOperationType(expression.getOperation());
        context.addInstruction(PL0InstructionType.OPR, context.getStackLevel(), operationCode);

        // Zde bude datovy typ vzdy boolean
        expression.setDataType(DataType.Boolean);
    }

    private void processFloatOperation(GeneratorContext context) {
        throw new NotImplementedException("TODO implement this feature");
    }

    /**
     * Helper funkce, ktera vrati string s hodnotou, pokud je vyraz literal nebo prazdny retezec
     */
    private static String getValueIfValueExpression(Expression expression) {
        if (expression instanceof ValueExpression) {
            return " with value: " + ((ValueExpression) expression).getValue();
        }

        return "";
    }


}
