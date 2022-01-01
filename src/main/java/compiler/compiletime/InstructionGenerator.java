package compiler.compiletime;

import compiler.compiletime.processor.statement.StatementProcessor;
import compiler.parsing.Entrypoint;
import compiler.parsing.statement.Statement;
import compiler.pl0.PL0Instruction;
import compiler.utils.CompileException;
import java.util.ArrayList;
import java.util.List;

public class InstructionGenerator {

    private final Entrypoint entrypoint;

    private final GeneratorContext rootContext;

    public InstructionGenerator(Entrypoint entrypoint) {
        this.entrypoint = entrypoint;
        this.rootContext = new GeneratorContext();
    }

    public List<PL0Instruction> generate() throws CompileException {
        var result = new ArrayList<PL0Instruction>();

        // Pridame prvni instrukci


        for (Statement statement : entrypoint.getStatements()) {
            new StatementProcessor(statement).process(rootContext);
        }


        GeneratorContext.getInstructions().forEach(System.out::println);

        return result;
    }

}
