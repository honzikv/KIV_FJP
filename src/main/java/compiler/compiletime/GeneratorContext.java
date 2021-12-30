package compiler.compiletime;

import compiler.parsing.FunctionDefinition;
import compiler.pl0.PL0Instruction;
import compiler.pl0.PL0InstructionType;
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
    private long stackLevel;

    @Getter
    @Setter
    private long stackPointerAddress;

    @Getter
    @Setter
    private long instructionNumber;

    @Getter
    private final List<PL0Instruction> instructions = new ArrayList<>();

    public boolean identifierExists(String identifier) {
        if (variables.containsKey(identifier) || functions.containsKey(identifier)) {
            return true;
        }

        return parentContext != null && parentContext.identifierExists(identifier);
    }

    public boolean variableExistsInCurrentScope(String identifier) {
        return variables.containsKey(identifier);
    }

    /**
     * Prida promennou do lookup tabulky, ale neprida ji na stack
     *
     * @param variable
     */
    public void addVariableToLookupTable(Variable variable) {
        variables.put(variable.getIdentifier(), variable);
    }


    /**
     * Ziska promennou z tohoto objektu a nebo rekurzivne z rodicu, pokud existuje
     *
     * @param identifier hledany identifikator
     * @return objekt, pokud existuje, jinak null
     */
    public Variable getVariableOrDefault(String identifier) {
        if (variables.containsKey(identifier)) {
            return variables.get(identifier);
        }

        return parentContext == null ? null : parentContext.getVariableOrDefault(identifier);
    }

    public GeneratorContext(long stackLevel, long stackPointerAddress, long currentInstruction) {
        this.stackLevel = stackLevel;
        this.stackPointerAddress = stackPointerAddress;
        this.instructionNumber = currentInstruction;
        this.variables = new HashMap<>();
    }

    public GeneratorContext(long stackLevel, long stackPointerAddress, long currentInstruction, GeneratorContext parent) {
        this(stackLevel, stackPointerAddress, currentInstruction);
        this.parentContext = parent;
    }

    /**
     * Prida instrukci do kontextu a zvysi instruction number
     *
     * @param instructionType  typ instrukce instrukce
     * @param stackLevel       uroven na zasobniku
     * @param instructionParam adresa / parametr
     */
    public void addInstruction(PL0InstructionType instructionType,
                               long stackLevel, long instructionParam) {
        var instruction = new PL0Instruction(instructionType, stackLevel, instructionNumber, instructionParam);
        instructions.add(instruction);
        instructionNumber += 1;
    }
}
