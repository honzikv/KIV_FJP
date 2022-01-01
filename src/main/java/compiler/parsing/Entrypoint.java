package compiler.parsing;

import compiler.parsing.statement.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Entrypoint {

    /**
     * Seznam vsech statementu v entrypointu
     */
    private final List<Statement> statements = new ArrayList<>();

    private final List<FunctionDefinition> functionDefinitions = new ArrayList<>();

    /**
     * Prida statement do seznamu
     *
     * @param statement
     */
    public void addStatement(Statement statement) { statements.add(statement); }

    public void addFunctionDefinition(FunctionDefinition functionDefinition) {
        functionDefinitions.add(functionDefinition);
    }
}
