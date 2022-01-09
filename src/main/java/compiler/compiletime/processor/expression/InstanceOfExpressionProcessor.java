package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.DataType;
import compiler.parsing.expression.InstanceOfExpression;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import java.util.Set;
import lombok.AllArgsConstructor;

/**
 * Processor pro instanceof operator
 */
@AllArgsConstructor
public class InstanceOfExpressionProcessor implements IProcessor {

    private final InstanceOfExpression instanceOfExpression;

    private final Set<String> legalDataTypes = Set.of("int", "bool", "float");

    private void printDataType(GeneratorContext context, DataType dataType) {
        context.addInstruction(PL0InstructionType.LIT, 0, dataType == instanceOfExpression.getRightSide()
                ? 1 : 0);
    }

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var identifier = instanceOfExpression.getIdentifier();
        instanceOfExpression.setDataType(DataType.Boolean);

        if (legalDataTypes.contains(identifier)) {
            printDataType(context, DataType.convertStringTypeToDataType(identifier));
            return;
        }

        var variable = context.getVariableOrDefault(identifier);
        if (variable == null) {
            throw new CompileException("Error, variable with identifier " + identifier + " does not exist!");
        }

        printDataType(context, variable.getDataType());
    }
}
