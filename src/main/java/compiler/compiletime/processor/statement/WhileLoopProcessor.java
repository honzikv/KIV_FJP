package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.parsing.DataType;
import compiler.parsing.statement.loop.DoWhileLoopStatement;
import compiler.parsing.statement.loop.WhileLoopStatement;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WhileLoopProcessor implements IProcessor {

    private WhileLoopStatement whileLoopStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        if (whileLoopStatement instanceof DoWhileLoopStatement) {
            processDoWhileLoop(context);
            return;
        }

        processWhileLoop(context);
    }

    /**
     * Metoda pro zpracovani while cyklu
     *
     * @param context
     * @throws CompileException
     */
    private void processWhileLoop(GeneratorContext context) throws CompileException {
        var blockScopeProcessor = new BlockScopeProcessor(whileLoopStatement.getBlockScope(), true,
                context.getStackLevel(), context);
        blockScopeProcessor.allocateSpace();

        var whileLoopStartIdx = context.getNextInstructionNumber(); // index pro start

        var whileExpression = whileLoopStatement.getExpression();

        // Zpracujeme vyraz
        var expressionProcessor = new ExpressionProcessor(whileExpression);
        expressionProcessor.process(context);

        // Zkontrolujeme zda-li je vyraz boolean
        if (whileExpression.getDataType() != DataType.Boolean) {
            throw new CompileException("Error, while expression must evaluate to boolean!");
        }

        var whileLoopExitIdx = context.getNextInstructionNumber();
        context.addInstruction(PL0InstructionType.JMC, 0, Long.MIN_VALUE);

        blockScopeProcessor.process(context);

        // Pridame podminku pro skok na zacatek
        context.addInstruction(PL0InstructionType.JMP, 0, whileLoopStartIdx);

        var nextInstructionIdx = context.getNextInstructionNumber();
        context.getInstruction(whileLoopExitIdx).setInstructionAddress(nextInstructionIdx);
    }

    /**
     * Metoda pro zpracovani do while cyklu
     *
     * @param context
     * @throws CompileException
     */
    private void processDoWhileLoop(GeneratorContext context) throws CompileException {
        // Zpracujeme blockScope
        var blockScopeProcessor = new BlockScopeProcessor(whileLoopStatement.getBlockScope(), true,
                context.getStackLevel(), context);
        blockScopeProcessor.allocateSpace();
        var whileLoopStartIdx = context.getNextInstructionNumber();

        blockScopeProcessor.process(context);

        var whileExpression = whileLoopStatement.getExpression();

        // Zpracujeme vyraz
        var expressionProcessor = new ExpressionProcessor(whileExpression);
        expressionProcessor.process(context);

        // Zkontrolujeme zda-li je vyraz boolean
        if (whileExpression.getDataType() != DataType.Boolean) {
            throw new CompileException("Error, while expression must evaluate to boolean!");
        }

        var whileLoopExitIdx = context.getNextInstructionNumber();
        context.addInstruction(PL0InstructionType.JMC, 0, Long.MIN_VALUE);

        // Pridame podminku pro skok na zacatek
        context.addInstruction(PL0InstructionType.JMP, 0, whileLoopStartIdx);

        var nextInstructionIdx = context.getNextInstructionNumber();
        context.getInstruction(whileLoopExitIdx).setInstructionAddress(nextInstructionIdx);
    }
}
