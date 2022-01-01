package compiler.compiletime;

import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
import compiler.compiletime.utils.StringUtils;
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
    private final long stackLevel;

    @Getter
    @Setter
    private long stackPointerAddress;

    @Getter
    @Setter
    private long instructionNumber;

    @Getter
    private final List<PL0Instruction> instructions = new ArrayList<>();

    public boolean functionExists(String identifier) {
        return functions.containsKey(identifier);
    }

    public void addFunction(FunctionDefinition function) {
        functions.put(function.getIdentifier(), function);
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

    public int getNextInstructionNumber() { return instructions.size(); }

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

        // Navic musime adekvatne upravit stack pointer
        switch (instructionType) {
            case LIT, LOD -> stackPointerAddress += 1;
            case INT -> stackPointerAddress += instructionParam;
            case STO, JMC, OPR -> stackPointerAddress -= 1;
        }
    }

    /**
     * Ziska danou instrukci
     *
     * @param idx
     * @return
     */
    public PL0Instruction getInstruction(int idx) {
        return instructions.get(idx);
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

        IntegerUtils.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += IntegerUtils.sizeOf();
    }

    public void allocateVariable(Variable variable, Float value) throws CompileException {
        if (variable.getDataType() != DataType.Float) {
            throw new CompileException("Error, trying to allocate float to a non-float variable");
        }

        FloatUtils.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += FloatUtils.sizeOf();
    }

    public void allocateVariable(Variable variable, Boolean value) throws CompileException {
        if (variable.getDataType() != DataType.Boolean) {
            throw new CompileException("Error, trying to allocate boolean to a non-boolean variable");
        }

        BooleanUtils.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += BooleanUtils.sizeOf();
    }

    public void allocateVariable(Variable variable, String value) throws CompileException {
        if (variable.getDataType() != DataType.String) {
            throw new CompileException("Error, trying to allocate string to a non-string variable");
        }

        StringUtils.addOnStack(this, value);
        variable.setAddress(stackPointerAddress);
        stackPointerAddress += StringUtils.sizeOf(value);
    }

    public FunctionDefinition getFunction(String identifier) {
        return functions.get(identifier);
    }

}
