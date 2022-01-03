package compiler.compiletime.processor;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.function.FunctionDefinitionProcessor;
import compiler.compiletime.processor.statement.BlockScopeProcessor;
import compiler.compiletime.processor.statement.StatementProcessor;
import compiler.parsing.Entrypoint;
import compiler.parsing.FunctionDefinition;
import compiler.parsing.statement.BlockScope;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import java.util.Comparator;
import lombok.AllArgsConstructor;

/**
 * Processor pro zpracovani entrypointu
 */
@AllArgsConstructor
public class EntrypointProcessor implements IProcessor {

    private final Entrypoint entrypoint;

    /**
     * Funkce zpracuje cely program
     *
     * @param context root context
     * @throws CompileException
     */
    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Protoze jazyk ma podporovat funkci ve funkci potrebujeme ve funkci nejakym zpusobem ulozit misto na
        // parametry, aby se mohli ulozit na spravne misto a nacist
        // Metoda najde maximalni pocet parametru s return hodnotou a nastavi je do kontextu, takze se vzdy pri danem
        // volani funkce alokuji. Slo by to samozrejme udelat i daleko inteligentneji, ale museli bychom slozite prochazet
        // v kazde funkci co se jak vola abychom ziskali mensi pocet parametru a tento zpusob by mel pro semestralni
        // praci stacit
        preprocessFunctions(context);

        // Inicializujeme misto pro SB, DB, PC + parametry
        context.addInstruction(PL0InstructionType.INT, 0, 3 + GeneratorContext.getParamsMaxSize());

        // Nasledne se provede skok, ktery skoci na vykonny kod programu, aby se nevolali funkce
        var jumpIdx = context.getNextInstructionNumber();
        context.addInstruction(PL0InstructionType.JMP, 0, Integer.MIN_VALUE);


        // Zpracujeme funkce (pokud jsou)
        var functionProcessor = new FunctionDefinitionProcessor();
        for (var functionDefinition : entrypoint.getFunctionDefinitions()) {
            functionProcessor.setFunctionDefinition(functionDefinition);
            functionProcessor.process(context);
        }

        // Ke stack pointeru pridame alokovane misto pro parametry
        context.setStackPointerAddress(context.getStackPointerAddress() + GeneratorContext.getParamsMaxSize());

        // Musime skocit sem
        var nextInstructionIdx = context.getNextInstructionNumber();
        context.getInstruction(jumpIdx).setInstructionAddress(nextInstructionIdx);

        // Alokujeme misto pro beh programu
        var blockScope = new BlockScope(0);
        entrypoint.getStatements().forEach(blockScope::addStatement);
        var blockScopeProcessor = new BlockScopeProcessor(blockScope, true, 0);
        blockScopeProcessor.allocateSpace(context);

        context.debugLog();

        // Zpracujeme statementy
        var statementProcessor = new StatementProcessor();
        for (var statement : entrypoint.getStatements()) {
            statementProcessor.setStatement(statement);
            statementProcessor.process(context);
        }

        // instrukce na dealokaci
        blockScopeProcessor.deallocateSpace(context);

        // dealokace parametru a navratovych hodnot
        var paramsSize = GeneratorContext.getParamsMaxSize();
        if (paramsSize != null) {
            context.addInstruction(PL0InstructionType.INT, 0, -paramsSize);
        }

        // Navrat z programu
        context.addInstruction(PL0InstructionType.RET, 0, 0);
    }

    private void preprocessFunctions(GeneratorContext context) throws CompileException {
        var functions = entrypoint.getFunctionDefinitions();

        // Ziskame nejvetsi pocet argumentu s return hodnotou
        // Pokud neni nastavime parametry na nulu, protoze se zadne parametry v kodu nevyskytuji
        functions.stream().max(Comparator.comparing(FunctionDefinition::getParamsWithReturnValSize))
                .ifPresentOrElse(
                        max -> context.updateParamSpaceRequirements(max.getParamsWithReturnValSize()),
                        () -> context.updateParamSpaceRequirements(0));

        if (GeneratorContext.getParamsMaxSize() == Integer.MAX_VALUE) {
            throw new CompileException("Error, some function has unspecified parameter type or return value");
        }
    }
}
