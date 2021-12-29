package compiler.runtime;

import compiler.parsing.statement.function.FunctionDeclaration;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabulka symbolu pro dany scope
 */
public class SymbolTable {

    private final Map<String, Variable> variables;

    private final Map<String, FunctionDeclaration> functions;

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

    public FunctionDeclaration getFunction(String identifier) {
        return functions.get(identifier);
    }

    public void addFunction(FunctionDeclaration function) {
        functions.put(function.getIdentifier(), function);
    }
}
