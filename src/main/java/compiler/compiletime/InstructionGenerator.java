package compiler.compiletime;

import compiler.parsing.Entrypoint;
import compiler.parsing.statement.Statement;
import compiler.pl0.PL0Instruction;
import compiler.pl0.PL0InstructionType;
import java.util.ArrayList;
import java.util.List;

public class InstructionGenerator {

    private final Entrypoint entrypoint;

    private final GeneratorContext rootContext;

    public InstructionGenerator(Entrypoint entrypoint) {
        this.entrypoint = entrypoint;
        this.rootContext = new GeneratorContext(0, 0, 1);
    }

    public List<PL0Instruction> generate() {
        var result = new ArrayList<PL0Instruction>();

        // Pridame prvni instrukci
        var initInstruction = PL0Instruction.create(
                rootContext, PL0InstructionType.JMP, 0, 1);
        rootContext.addInstruction(initInstruction);

        entrypoint.getChildStatements().forEach(statement -> result.addAll(resolveStatement(statement)));

        return result;
    }

    public List<PL0Instruction> resolveStatement(Statement statement) {


        return new ArrayList<>();
    }
}
