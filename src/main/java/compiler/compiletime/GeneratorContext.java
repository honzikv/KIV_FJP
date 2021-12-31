package compiler.compiletime;

import compiler.compiletime.libs.BooleanLib;
import compiler.compiletime.libs.FloatLib;
import compiler.compiletime.libs.IntegerLib;
import compiler.compiletime.libs.StringLib;
import compiler.parsing.DataType;
import compiler.parsing.FunctionDefinition;
import compiler.pl0.PL0Instruction;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
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

    /**
     * Inkrementace stack pointeru
     */
    public void incrementStackPointer() {
        this.stackPointerAddress += 1;
    }

    /**
     * Zvysi adresu stack pointeru o dany pocet
     *
     * @param amount pocet, o ktery se ma adresa zvysit
     */
    public void increaseStackPointer(int amount) {
        this.stackPointerAddress += amount;
    }

    /**
     * Dekrementace stack pointeru
     */
    public void decrementStackPointer() {
        this.stackPointerAddress -= 1;
    }

    /**
     * Snizi adresu stack pointeru o dany pocet
     *
     * @param amount pocet, o ktery se ma adresa snizit
     */
    public void decreaseStackPointer(int amount) {
        this.stackPointerAddress -= amount;
    }

    public void allocateVariable(Variable variable) throws CompileException {
        switch (variable.getDataType()) {
            case Int -> allocateVariable(variable, 0);
            case Float -> allocateVariable(variable, 0.0f);
            case Boolean -> allocateVariable(variable, false);
            default -> throw new CompileException("Error, invalid data type present");
        }
    }

    public void allocateVariable(Variable variable, Integer value) throws CompileException {
        if (variable.getDataType() != DataType.Int) {
            throw new CompileException("Error, trying to allocate integer to a non-integer variable");
        }

        IntegerLib.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += IntegerLib.sizeOf();
    }

    public void allocateVariable(Variable variable, Float value) throws CompileException {
        if (variable.getDataType() != DataType.Float) {
            throw new CompileException("Error, trying to allocate float to a non-float variable");
        }

        FloatLib.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += FloatLib.sizeOf();
    }

    public void allocateVariable(Variable variable, Boolean value) throws CompileException {
        if (variable.getDataType() != DataType.Boolean) {
            throw new CompileException("Error, trying to allocate boolean to a non-boolean variable");
        }

        BooleanLib.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += BooleanLib.sizeOf();
    }

    public void allocateVariable(Variable variable, String value) throws CompileException {
        if (variable.getDataType() != DataType.String) {
            throw new CompileException("Error, trying to allocate string to a non-string variable");
        }

        StringLib.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += StringLib.sizeOf(value);
    }
}
