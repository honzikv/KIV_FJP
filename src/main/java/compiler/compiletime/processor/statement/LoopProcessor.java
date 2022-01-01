package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.loop.DoWhileLoopStatement;
import compiler.parsing.statement.loop.ForLoopStatement;
import compiler.parsing.statement.loop.RepeatUntilLoopStatement;
import compiler.parsing.statement.loop.WhileLoopStatement;
import compiler.utils.CompileException;

/**
 * Implementace pro for, while, do while a repeat until
 */
public class LoopProcessor implements IProcessor {

    private ForLoopStatement forLoopStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // 2030 pujde udelat switch na instanceof
        if (forLoopStatement instanceof WhileLoopStatement) {
            processWhileLoop(context, (WhileLoopStatement) forLoopStatement);
            return;
        }

        if (forLoopStatement instanceof DoWhileLoopStatement) {
            processDoWhileLoop(context, (DoWhileLoopStatement) forLoopStatement);
            return;
        }

        if (forLoopStatement instanceof RepeatUntilLoopStatement) {
            processRepeatUntilLoop(context, (RepeatUntilLoopStatement) forLoopStatement);
            return;
        }

        processForLoop(context, forLoopStatement);
    }

    private void processWhileLoop(GeneratorContext context, WhileLoopStatement whileLoop) { }

    private void processDoWhileLoop(GeneratorContext context, DoWhileLoopStatement doWhileLoop) { }

    private void processRepeatUntilLoop(GeneratorContext context, RepeatUntilLoopStatement repeatUntilLoop) { }

    private void processForLoop(GeneratorContext context, ForLoopStatement forLoop) { }


}
