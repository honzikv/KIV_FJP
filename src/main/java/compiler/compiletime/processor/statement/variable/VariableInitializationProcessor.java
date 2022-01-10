package compiler.compiletime.processor.statement.variable;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.utils.CompileException;
import java.util.ArrayList;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class VariableInitializationProcessor implements IProcessor {

    private final VariableInitializationStatement variableStatement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var variable = context.getVariableOrDefault(variableStatement.getIdentifier());

        // Tento if slouzi pro debug - pri normalnim pouzivani by mela byt promena vzdy v kontextu
        if (variable == null) {
            throw new CompileException("Error, variable with identifier " + variable.getIdentifier() + " does not exist");
        }

        if (variable.isDeclared()) {
            throw new CompileException("Error, cannot redeclare already declared variable! (" + variable.getIdentifier() + ")");
        } else {
            variable.setDeclared(true);
        }

//        // Pokud nebyla promena jeste deklarovana nastavime flag na true
//        if (!variable.isDeclared()) {
//            variable.setDeclared(true);
//        }

        // Pokud je statement konstatni nastavime na true
        if (variableStatement.isConst()) {
            variable.setConst(true);
        }

        // Pro jednoduchost nechame assignment zpracovat processor pro assignment
        // Takze vytvorime novy objekt jako kdyby se jednalo o prirazeni
        var variableAssignment = variableStatement.isLiteralValue()
                ? new VariableAssignmentStatement(variableStatement.getDepthLevel(),
                variable.getIdentifier(), new ArrayList<>(), variableStatement.getLiteralValue())
                : new VariableAssignmentStatement(variableStatement.getDepthLevel(),
                variable.getIdentifier(), new ArrayList<>(), variableStatement.getExpression());

        // Assignment processor nastavi zbytek jako flagy na inicializovani a ulozeni na spravne adresy
        var variableAssignmentProcessor = new VariableAssignmentProcessor(variableAssignment);
        variableAssignmentProcessor.process(context);
    }

}
