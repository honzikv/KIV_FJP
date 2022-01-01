package compiler.compiletime;

import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
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
 * Kontext compileru - kazdy scope ma svuj vlastni
 */
public class GeneratorContext {

    /**
     * Deklararace funkci - toto je staticke, protoze funkce lze definovat pouze na levelu 0 a
     * nechceme mit funkci ve funkci
     */
    private static final Map<String, FunctionDefinition> functions = new HashMap<>();

    /**
     * Reference na rodicovsky kontext - muze byt i null, pokud je tento kontext root
     * Pres tento kontext prohledame, zda-li existuje nejaka promenna
     */
    private GeneratorContext parentContext;

    /**
     * Lokalni promenne (nebo globalni v zavislosti na urovni zanoreni)
     */
    private final Map<String, Variable> variables;

    @Getter
    private final int stackLevel;

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

    public GeneratorContext() {
        this.stackLevel = 0;
        this.stackPointerAddress = 0;
        this.instructionNumber = 0;
        this.variables = new HashMap<>();
    }

    public GeneratorContext(int stackLevel, long stackPointerAddress, long currentInstruction) {
        this.stackLevel = stackLevel;
        this.stackPointerAddress = stackPointerAddress;
        this.instructionNumber = currentInstruction;
        this.variables = new HashMap<>();
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

    public GeneratorContext(int stackLevel, GeneratorContext parent) {
        this(stackLevel, parent.stackPointerAddress, parent.instructionNumber);
        this.parentContext = parent;
    }

    public boolean variableDeclaredInCurrentScope(String identifier) {
        if (!variables.containsKey(identifier)) {
            return false;
        }

        // Ziskame promennou a vratime, zda-li je deklarovana
        return variables.get(identifier).isDeclared();
    }

    /**
     * Deklaruje promennou v danem kontextu
     *
     * @param identifier identifikator promenne
     * @throws CompileException pokud promenna neni v danem kontextu
     */
    public void declareVariable(String identifier) throws CompileException {
        if (!variables.containsKey(identifier)) {
            throw new CompileException("Error, variable with identifier: " + identifier + " was not found");
        }
        var variable = variables.get(identifier);
        variable.setDeclared(true);
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
     * @param idx index instrukce
     * @return danou instrukci nebo vyhodi IndexOutOfBounds pokud je index mimo
     */
    public PL0Instruction getInstruction(int idx) {
        return instructions.get(idx);
    }

    public void allocateVariable(String identifier, DataType dataType) throws CompileException {
        switch (dataType) {
            case Int -> allocateVariable(identifier, 0);
            case Float -> allocateVariable(identifier, 0.0f);
            case Boolean -> allocateVariable(identifier, false);
            default -> throw new CompileException("Error, invalid data type present");
        }
    }

    public void allocateVariable(String identifier, Integer value) throws CompileException {
        if (variables.containsKey(identifier)) {
            throw new CompileException("Error, reallocation of existing variable in the same scope!");
        }
        var variable = new Variable(identifier, stackPointerAddress, DataType.Int);
        IntegerUtils.addOnStack(this, value);
        variable.setStackLevel(stackLevel);
        variables.put(identifier, variable);
    }

    public void allocateVariable(String identifier, Float value) throws CompileException {
        if (variables.containsKey(identifier)) {
            throw new CompileException("Error, reallocation of existing variable in the same scope!");
        }
        var variable = new Variable(identifier, stackPointerAddress, DataType.Float);
        FloatUtils.addOnStack(this, value);
        variable.setStackLevel(stackLevel);
        variables.put(identifier, variable);
    }

    public void allocateVariable(String identifier, Boolean value) throws CompileException {
        if (variables.containsKey(identifier)) {
            throw new CompileException("Error, reallocation of existing variable in the same scope!");
        }
        var variable = new Variable(identifier, stackPointerAddress, DataType.Boolean);
        BooleanUtils.addOnStack(this, value);
        variable.setStackLevel(stackLevel);
        variables.put(identifier, variable);
    }

    public FunctionDefinition getFunction(String identifier) {
        return functions.get(identifier);
    }

    /**
     * Provede pohlceni daneho kontextu - vezme si jeho instrukce a nastavi si jeho parametry krome
     * tabulky promennych, kterou zahodi. Timto muze mit kazdy scope promenne se stejnym jmenem
     * a nedojde k chybe
     *
     * @param context kontext, ktery se pohlti. Tento kontext se musi zahodit po zavolani teto metody
     */
    public void devourChildContext(GeneratorContext context) {
        instructions.addAll(context.instructions);
        stackPointerAddress = context.stackPointerAddress;
        instructionNumber = context.instructionNumber;
    }
}
