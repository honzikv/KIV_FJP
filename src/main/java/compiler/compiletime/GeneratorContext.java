package compiler.compiletime;

import compiler.parsing.FunctionDefinition;
import compiler.pl0.PL0Instruction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Kontext compileru
 */
public class GeneratorContext {

    /**
     * Deklararace funkci - toto je staticke, protoze nelze definovat funkci ve funkci
     */
    private static final Map<String, FunctionDefinition> functions = new HashMap<>();

    /**
     * Reference na rodicovsky kontext - muze byt i null, pokud je tento kontext root
     * Pres tento kontext prohledame, zda-li existuje nejaka promenna
     */
    private GeneratorContext parentContext;

    /**
     * Lokalni promenne
     */
    private final Map<String, Variable> variables;

    @Getter
    @Setter
    private long depth;

    @Getter
    @Setter
    private long stackPointerAddress;

    @Getter
    @Setter
    private long instructionNumber;

    @Getter
    private final List<PL0Instruction> instructions = new ArrayList<>();

    public void addInstruction(PL0Instruction instruction) {
        instructions.add(instruction);
    }

    public void incrementInstructionNumber() {
        instructionNumber += 1;
    }

    public void addInstructions(List<PL0Instruction> instructionList) {
        instructions.addAll(instructionList);
    }

    public boolean identifierExists(String identifier) {
        return variables.containsKey(identifier) || functions.containsKey(identifier);
    }

    public GeneratorContext(long depth, long stackPointerAddress, long currentInstruction) {
        this.depth = depth;
        this.stackPointerAddress = stackPointerAddress;
        this.instructionNumber = currentInstruction;
        this.variables = new HashMap<>();
    }

    public GeneratorContext(long depth, long stackPointerAddress, long currentInstruction, GeneratorContext parent) {
        this(depth, stackPointerAddress, currentInstruction);
        this.parentContext = parent;
    }

}
