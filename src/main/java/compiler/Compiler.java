package compiler;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.processor.EntrypointProcessor;
import compiler.utils.CompileException;
import compiler.utils.IOProvider;
import main.antlr4.grammar.CMMLexer;
import main.antlr4.grammar.CMMParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.visitor.EntrypointVisitor;

/**
 * Objekt prekladace, ktery se vola z mainu a provede preklad ze vstupu
 */
public class Compiler {

    /**
     * Metoda pro spusteni prekladu
     */
    public void run() throws CompileException {
        // Ziskame vstup do compileru
        var input = IOProvider.read();
        compile(input);
    }

    /**
     * Metoda pro samotnou kompilaci instrukci
     *
     * @param input
     */
    private void compile(String input) throws CompileException {
        // Vytvorime lexer a parser
        var lexer = new CMMLexer(CharStreams.fromString(input));
        var parser = new CMMParser(new CommonTokenStream(lexer));

        // Entrypoint je vstup cele gramatiky, takze staci navstivit ten a rekurzivne resolvujeme zbytek
        var entrypointVisitor = new EntrypointVisitor();
        var entrypoint = entrypointVisitor.visit(parser.entrypoint());


        var context = new GeneratorContext();
        var entrypointProcessor = new EntrypointProcessor(entrypoint);
        entrypointProcessor.process(context);

        // Ziskame instrukce a vypiseme je do stdoutu
        var instructions = GeneratorContext.getInstructions();
        for (var i = 0; i < instructions.size(); i += 1) {
            var instruction = instructions.get(i);
            System.out.println(i + " " + instruction);
        }
    }
}
