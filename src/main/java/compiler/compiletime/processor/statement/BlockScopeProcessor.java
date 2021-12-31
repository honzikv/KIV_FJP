package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.BlockScope;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockScopeProcessor implements IProcessor {

    private final BlockScope blockScope;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Zpracovani bloku znamena pouze zpracovat jednotlive statementy, ktere jsou vzdy jednoduche prikazy
        // nebo volani metod / control flow
        var statementProcessor = new StatementProcessor();
        for (var statement : blockScope.getChildStatements()) {
            statementProcessor.process(context, statement);
        }
    }
}
