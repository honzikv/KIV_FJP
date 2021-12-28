package compiler.language;

import compiler.language.statement.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Entrypoint {

    /**
     * Seznam vsech statementu v entrypointu
     */
    @Getter
    private final List<Statement> childStatements = new ArrayList<>();

    /**
     * Prida statement do seznamu
     * @param statement
     */
    public void addStatement(Statement statement) { childStatements.add(statement); }
}
