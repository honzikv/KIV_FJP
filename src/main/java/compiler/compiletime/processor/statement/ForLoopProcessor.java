package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.compiletime.utils.IntegerUtils;
import compiler.parsing.DataType;
import compiler.parsing.expression.OperationType;
import compiler.parsing.statement.loop.ForLoopStatement;
import compiler.pl0.PL0InstructionType;
import compiler.pl0.PL0Utils;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

/**
 * Implementace pro for, while, do while a repeat until
 */
@AllArgsConstructor
public class ForLoopProcessor implements IProcessor {

    private ForLoopStatement forLoopStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Pro for cyklus mame kontrolni promennou uz nastavenou od BlockScope processoru, takze staci vypsat kod
        // s kontrolou
        var expressionProcessor = new ExpressionProcessor();

        // Ziskame iteracni promennou - ta musi byt v aktualnim scopu, jinak bychom modifikovali neco uplne mimo
        if (!context.variableDeclaredInCurrentScope(forLoopStatement.getIdentifier())) {
            throw new CompileException("Error, iteration variable for for loop was not declared!");
        }
        var iterationVariable = context.getVariableOrDefault(forLoopStatement.getIdentifier());
        iterationVariable.setInitialized(true);

        // Nacteme from a ulozime do iteracni promenne
        var from = forLoopStatement.getStart();
        expressionProcessor.process(context, forLoopStatement.getStart());

        // Pokud neni vysledek vyrazu int, nelze dale pokracovat
        if (from.getDataType() != DataType.Int) {
            throw new CompileException("Error, expression in start of for loop does not evaluate to integer!");
        }

        // Nacteme vyraz do promenne
        IntegerUtils.storeToStackAddress(context, iterationVariable.getAddress());

        var forLoopStartInstructionIdx = context.getNextInstructionNumber();

        // Tady by to teoreticky slo optimalizovat pomocnou promennou - ale tezko rict obecne, takze
        // to pri kazdem cyklu znova prepocte vyraz
        var to = forLoopStatement.getEnd();
        expressionProcessor.process(context, to);

        if (to.getDataType() != DataType.Int) {
            throw new CompileException("Error, expression in end of for loop does not evaluate to integer!");
        }

        // Na stacku mame vyraz a muzeme tam hodit i hodnotu z promenne
        context.addInstruction(PL0InstructionType.LOD, 0, iterationVariable.getAddress());

        // Provedeme porovnani
        context.addInstruction(PL0InstructionType.OPR, 0,
                PL0Utils.getOperationNumberFromOperationType(OperationType.GreaterThan));

        // Index instrukce, ktera skoci pri rovnosti
        var forLoopExitInstructionIdx = context.getNextInstructionNumber();
        // Skocime nekam
        context.addInstruction(PL0InstructionType.JMC, 0, Long.MIN_VALUE);

        // Zpracujeme blok
        var blockScopeProcessor = new BlockScopeProcessor(forLoopStatement.getBlockScope(), true,
                context.getStackLevel());
        blockScopeProcessor.process(context);

        // Inkrementace o 1
        context.addInstruction(PL0InstructionType.LOD, 0, iterationVariable.getAddress());
        context.addInstruction(PL0InstructionType.LIT, 0, 1);
        context.addInstruction(PL0InstructionType.OPR, 0,
                PL0Utils.getOperationNumberFromOperationType(OperationType.Addition));

        // Ulozime vysledek do promenne
        context.addInstruction(PL0InstructionType.STO, 0, iterationVariable.getAddress());

        // A presuneme se na zacatek hlavicky
        context.addInstruction(PL0InstructionType.JMP, 0, forLoopStartInstructionIdx);

        // Nyni muzeme upravit instrukci JMC
        var nextInstructionIdx = context.getNextInstructionNumber();
        context.getInstruction(forLoopExitInstructionIdx).setInstructionAddress(nextInstructionIdx);

    }

}
