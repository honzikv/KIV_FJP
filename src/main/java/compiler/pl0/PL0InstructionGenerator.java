package compiler.pl0;

import compiler.parsing.Entrypoint;
import compiler.parsing.statement.Statement;
import compiler.runtime.SymbolTable;
import java.util.ArrayList;
import java.util.List;

public class PL0InstructionGenerator {

    private final Entrypoint entrypoint;

    private int returnAddress;

    /**
     * Globalni tabulka symbolu
     */
    private final SymbolTable symbolTable;

    public PL0InstructionGenerator(Entrypoint entrypoint) {
        this.entrypoint = entrypoint;
        this.symbolTable = new SymbolTable();
    }

    public List<PL0Instruction> generate() {
        var result = new ArrayList<PL0Instruction>();
        entrypoint.getChildStatements().forEach(statement -> result.addAll(resolveStatement(statement)));

        return result;
    }

    public List<PL0Instruction> resolveStatement(Statement statement) {


        return new ArrayList<>();
    }
}
