package compiler.compiletime.processor;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.function.FunctionDefinitionProcessor;
import compiler.compiletime.processor.statement.BlockScopeProcessor;
import compiler.compiletime.processor.statement.StatementProcessor;
import compiler.parsing.Entrypoint;
import compiler.parsing.statement.BlockScope;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
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
        // Inicializujeme misto pro SB, DB, PC a jeste "return pointer" kam se uklada pri function callu adresa navratu
        context.addInstruction(PL0InstructionType.INT, 0, 3);

        // Krome inicializace zakladniho mista musime jeste vytvorit misto pro parametry a navratove hodnoty funkci
        // Protoze zatim nevime jak velke bude, nastavime ho na 0 a pri prekladu funkci se tam nastavi nejvetsi hodnota
        context.updateParamSpaceRequirements(0);

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
        context.setStackPointerAddress(context.getStackPointerAddress() + GeneratorContext.getParamsSize());

        // Musime skocit sem
        var nextInstructionIdx = context.getNextInstructionNumber();
        context.getInstruction(jumpIdx).setInstructionAddress(nextInstructionIdx);

        // Alokujeme misto pro beh programu
        var blockScope = new BlockScope(0);
        entrypoint.getStatements().forEach(blockScope::addStatement);
        var blockScopeProcessor = new BlockScopeProcessor(blockScope, true, 0);
        blockScopeProcessor.allocateSpace(context);

//        context.debugLog();

        // Zpracujeme statementy
        var statementProcessor = new StatementProcessor();
        for (var statement : entrypoint.getStatements()) {
            statementProcessor.setStatement(statement);
            statementProcessor.process(context);
        }

        // instrukce na dealokaci
        blockScopeProcessor.deallocateSpace(context);

        // dealokace parametru a navratovych hodnot
        var paramsSize = GeneratorContext.getParamsSize();
        if (paramsSize != null) {
            context.addInstruction(PL0InstructionType.INT, 0, -paramsSize);
        }

        // Navrat z programu
        context.addInstruction(PL0InstructionType.RET, 0, 0);
    }
}
