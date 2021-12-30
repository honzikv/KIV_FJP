package compiler;

import compiler.compiletime.InstructionGenerator;
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
    public void run() {
        // Ziskame vstup do compileru
        var input = IOProvider.read();
        compile(input);
    }

    /**
     * Metoda pro samotnou kompilaci instrukci
     * @param input
     */
    private void compile(String input) {
        // Vytvorime lexer a parser
        var lexer = new CMMLexer(CharStreams.fromString(input));
        var parser = new CMMParser(new CommonTokenStream(lexer));

        // Entrypoint je vstup cele gramatiky, takze staci navstivit ten a rekurzivne resolvujeme zbytek
        var entrypointVisitor = new EntrypointVisitor();
        var entrypoint = entrypointVisitor.visit(parser.entrypoint());

        entrypoint.getChildStatements().forEach(System.out::println);

        var instructionGenerator = new InstructionGenerator(entrypoint);
        var instructions = instructionGenerator.generate();
    }
}
