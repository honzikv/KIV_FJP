package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.DataType;
import compiler.parsing.expression.OperationType;
import compiler.parsing.expression.UnaryOperationExpression;
import compiler.pl0.PL0InstructionType;
import compiler.pl0.PL0Utils;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Visitor pro unarni vyraz
 */
@AllArgsConstructor
public class UnaryOperationExpressionProcessor implements IProcessor {

    private final UnaryOperationExpression expression;


    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Resolve child vyrazu
        var childExpression = expression.getChildExpression();
        var expressionProcessor = new ExpressionProcessor(childExpression);
        expressionProcessor.process(context);

        // Zpracovani NOT operace
        if (expression.getOperation() == OperationType.BooleanNot
                && childExpression.getDataType() == DataType.Boolean) {
            expression.setDataType(DataType.Boolean);

            // Na stack pridame 0 a porovname s predchozi hodnotou
            // Pokud byla predchozi hodnota 0, pak bude vysledek na stacku 1 a pokud byla 1, pak bude na stacku 0
            context.addInstruction(PL0InstructionType.LIT, 0, 0);
            context.addInstruction(PL0InstructionType.OPR, 0,
                    PL0Utils.getOperationNumberFromOperationType(OperationType.Equal));
            return;
        }

        // Pro unarni plus nic nedelame
        if (expression.getOperation() == OperationType.UnaryPlus
                && (childExpression.getDataType() == DataType.Int
                || childExpression.getDataType() == DataType.Float)) {
            expression.setDataType(childExpression.getDataType());
            return;
        }

        if (expression.getOperation() == OperationType.UnaryMinus
                && childExpression.getDataType() == DataType.Int) {
            expression.setDataType(childExpression.getDataType());
            context.addInstruction(PL0InstructionType.OPR, 0,
                    PL0Utils.getOperationNumberFromOperationType(OperationType.UnaryMinus));
            return;
        }

        if (expression.getOperation() == OperationType.UnaryMinus
                && childExpression.getDataType() == DataType.Float) {
            expression.setDataType(childExpression.getDataType());
            throw new NotImplementedException("This feature is not implemented");
        }

        throw new CompileException("Error, invalid unary operation present");
    }
}
