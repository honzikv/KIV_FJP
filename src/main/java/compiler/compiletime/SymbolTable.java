package compiler.compiletime;

import compiler.parsing.FunctionDefinition;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabulka symbolu pro dany scope
 */
public class SymbolTable {

    private final Map<String, Variable> variables;

    private final Map<String, FunctionDefinition> functions;

    public SymbolTable() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public boolean hasVariable(String identifier) {
        return variables.containsKey(identifier);
    }

    public Variable getVariable(String identifier) {
        return variables.get(identifier);
    }

    public void addVariable(Variable variable) {
        variables.put(variable.getIdentifier(), variable);
    }

    public boolean hasFunction(String identifier) {
        return functions.containsKey(identifier);
    }

    public FunctionDefinition getFunction(String identifier) {
        return functions.get(identifier);
    }

    public void addFunction(FunctionDefinition function) {
        functions.put(function.getIdentifier(), function);
    }
}
