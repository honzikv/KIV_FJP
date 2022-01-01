package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.parsing.DataType;
import compiler.parsing.statement.IfStatement;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IfStatementProcessor implements IProcessor {

    private final IfStatement ifStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var expression = ifStatement.getExpression();

        // Zpracujeme vyraz
        var expressionProcessor = new ExpressionProcessor(expression);
        expressionProcessor.process(context);

        if (expression.getDataType() != DataType.Boolean) {
            throw new CompileException("Error, if statement condition must be evaluated as boolean!");
        }

        // Ulozime si index pro instrukci, abychom se mohli k nemu vratit - pro upraveni skoku
        var ifIdx = context.getNextInstructionNumber();
        context.addInstruction(PL0InstructionType.JMC, 0, Long.MIN_VALUE);

        // Generujeme kod v ifu
        var blockScopeProcessor = new BlockScopeProcessor(ifStatement.getIfBlockScope(), true,
                context.getStackLevel());
        blockScopeProcessor.process(context);

        // Pokud kod navic obsahuje else udelame to same pro else
        var elseIdx = context.getNextInstructionNumber();
        if (ifStatement.getElseBlockScope() != null) {
            context.addInstruction(PL0InstructionType.JMP, 0, Long.MIN_VALUE);

            // Nyni z bloku ifu chceme skocit za instrukci JMP - tzn. na adresu dalsi instrukce
            context.getInstruction(ifIdx).setInstructionAddress(context.getNextInstructionNumber());

            // Zpracujeme co je v else bloku
            var elseBlockScopeProcessor = new BlockScopeProcessor(ifStatement.getElseBlockScope(), true,
                    context.getStackLevel());
            elseBlockScopeProcessor.process(context);

            // A nastavime instrukci
            var currentInstruction = context.getNextInstructionNumber();
            context.getInstruction(elseIdx).setInstructionAddress(currentInstruction);
        } else {
            // else neni, takze chceme skocit za aktualni instrukci - tzn. na adresu dalsi instrukce
            context.getInstruction(ifIdx).setInstructionAddress(context.getNextInstructionNumber());
        }
    }
}
