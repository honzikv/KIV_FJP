package compiler.compiletime;

import compiler.compiletime.utils.BooleanUtils;
import compiler.compiletime.utils.FloatUtils;
import compiler.compiletime.utils.IntegerUtils;
import compiler.parsing.DataType;
import compiler.parsing.FunctionDefinition;
import compiler.pl0.PL0Instruction;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import compiler.utils.Debug;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Kontext compileru - kazdy blok kodu ma svuj vlastni.
 * Kontexty sdileji seznam, kam ukladaji instrukce + adresu na stack pointer a cislo aktualni instrukce
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
     * Lokalni promenne
     */
    private final Map<String, Variable> variables;

    @Getter
    private final int stackLevel;

    /**
     * Instrukce jsou "globalni" - protoze jinak by bylo slozite slucovani kontextu
     */
    @Getter
    private static final List<PL0Instruction> instructions = new ArrayList<>();
    /**
     * Index adresy s parametry - ta je staticky od indexu 4 az do 4 + functionParametersSize
     */
    @Getter
    private static final int ParamsAddressIdx = 3;

    /**
     * Cislo nasledujici instrukce
     */
    @Getter
    @Setter
    private static long instructionNumber = 0;

    /**
     * Maximalni velikost parametru
     */
    @Getter
    private static Integer paramsMaxSize = 0;

    /**
     * Index adresy vrcholu zasobniku
     */
    @Getter
    @Setter
    private long stackPointerAddress = 0;

    /**
     * NoArgs konstruktor, ktery se pouziva v EntrypointProcessoru
     */
    public GeneratorContext() {
        this.stackLevel = 0;
        this.variables = new HashMap<>();
    }

    public void addFunction(FunctionDefinition function) {
        functions.put(function.getIdentifier(), function);
    }

    /**
     * Specialni konstruktor pro funkci, ktery se k rodicovi nepripoji pokud je attachToParent false
     * Timto zabranime referencim na "globalni" promenne z funkci, takze funkce budou "pure"
     *
     * @param stackLevel
     * @param parent
     * @param attachToParent
     */
    public GeneratorContext(int stackLevel, GeneratorContext parent, boolean attachToParent) {
        this(stackLevel);
        if (attachToParent) {
            this.parentContext = parent;
        }
        this.stackPointerAddress = parent.stackPointerAddress;
    }

    /**
     * Vrati true, pokud funkce existuje, jinak false
     *
     * @param identifier identifikator funkce
     * @return true, pokud fce existuje
     */
    public boolean functionExists(String identifier) {
        return functions.containsKey(identifier);
    }

    public GeneratorContext(int stackLevel) {
        this.stackLevel = stackLevel;
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
        this(stackLevel);
        this.parentContext = parent;
    }

    public void updateParamSpaceRequirements(int size) {
        if (paramsMaxSize < size) {
            paramsMaxSize = size;
        }
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
        var instruction = new PL0Instruction(instructionType, stackLevel, instructionParam);
        if (Debug.UseDebug) {
            System.out.println("Adding new instruction (" + instruction + ") " + instruction);
        }
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
        allocateVariable(identifier, dataType, false);
    }

    /**
     * Alokuje danou promennou na stacku
     *
     * @param identifier   identifikator
     * @param dataType     datovy typ
     * @param ignoreExists zda-li ma vyhodit vyjimku pokud promenna uz existuje - true se pouziva pro for cykly, aby
     *                     slo pouzit stejnou iteracni promennou vicekrat
     * @throws CompileException
     */
    public void allocateVariable(String identifier, DataType dataType, boolean ignoreExists) throws CompileException {
        if (!ignoreExists && variables.containsKey(identifier)) {
            throw new CompileException("Error, reallocation of existing variable in the same scope!");
        }
        switch (dataType) {
            case Int -> allocateVariable(identifier, 0);
            case Float -> allocateVariable(identifier, 0.0f);
            case Boolean -> allocateVariable(identifier, false);
            default -> throw new CompileException("Error, invalid data type present");
        }
    }


    public void allocateVariable(String identifier, Integer value) {
        var variable = new Variable(identifier, stackPointerAddress, DataType.Int);
        IntegerUtils.addOnStack(this, value);
        variables.put(identifier, variable);
    }

    public void allocateVariable(String identifier, Float value) {
        var variable = new Variable(identifier, stackPointerAddress, DataType.Float);
        FloatUtils.addOnStack(this, value);
        variables.put(identifier, variable);
    }

    public void allocateVariable(String identifier, Boolean value) {
        var variable = new Variable(identifier, stackPointerAddress, DataType.Boolean);
        BooleanUtils.addOnStack(this, value);
        variables.put(identifier, variable);
    }

    public FunctionDefinition getFunction(String identifier) {
        return functions.get(identifier);
    }

    /**
     * Pohlti dany kontext - vola se jako posledni metoda dane instance kontextu, aby se spravne nastavil
     * stack pointer - napr. pri zpracovavani funkci
     *
     * @param context kontext, ktery se pohlti touto instanci
     */
    public void consumeContext(GeneratorContext context) {
        this.stackPointerAddress = context.stackPointerAddress;
    }

    public void debugLog() {
        variables.forEach((__, variable) -> {
            System.out.println(variable);
        });
    }
}
