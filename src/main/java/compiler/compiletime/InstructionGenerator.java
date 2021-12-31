package compiler.compiletime;

import compiler.compiletime.processor.statement.StatementProcessor;
import compiler.parsing.Entrypoint;
import compiler.parsing.statement.Statement;
import compiler.pl0.PL0Instruction;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import java.util.ArrayList;
import java.util.List;

public class InstructionGenerator {

    private final Entrypoint entrypoint;

    private final GeneratorContext rootContext;

    public InstructionGenerator(Entrypoint entrypoint) {
        this.entrypoint = entrypoint;
        this.rootContext = new GeneratorContext(0, 0, 1);
    }

    public List<PL0Instruction> generate() throws CompileException {
        var result = new ArrayList<PL0Instruction>();

        // Pridame prvni instrukci
        rootContext.addInstruction(PL0InstructionType.JMP, 0, 1);

        for (Statement statement : entrypoint.getChildStatements()) {
            new StatementProcessor(statement).process(rootContext);
        }

        rootContext.getInstructions().forEach(System.out::println);

        return result;
    }

}
