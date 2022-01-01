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
        context.addInstruction(PL0InstructionType.INT, 0, 3);

        // Trochu osklive, ale vytvorime processor pro blockScope a zavolame funkci pro alokaci promennych
        var blockScope = new BlockScope(0);
        entrypoint.getStatements().forEach(blockScope::addStatement);
        var blockScopeProcessor = new BlockScopeProcessor(blockScope, true, 0, context);
        blockScopeProcessor.allocateSpace(context);

        // Zpracujeme funkce (pokud jsou)
        var functionProcessor = new FunctionDefinitionProcessor();
        for (var functionDefinition : entrypoint.getFunctionDefinitions()) {
            functionProcessor.setFunctionDefinition(functionDefinition);
            functionProcessor.process(context);
        }

        // Zpracujeme statementy - jazyk podporuje top-level statementy
        var statementProcessor = new StatementProcessor();
        for (var statement : entrypoint.getStatements()) {
            statementProcessor.setStatement(statement);
            statementProcessor.process(context);
        }

        // instrukce na ukonceni
        context.addInstruction(PL0InstructionType.RET, 0, 0);
    }
}
