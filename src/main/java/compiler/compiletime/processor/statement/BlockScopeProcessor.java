package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.utils.CompileException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;

@AllArgsConstructor
public class BlockScopeProcessor implements IProcessor {

    private final BlockScope blockScope;

    private boolean useParentContext;

    private int stackLevel;

    @Override
    public void process(GeneratorContext parentContext) throws CompileException {
        // Zpracovani bloku znamena pouze zpracovat jednotlive statementy, ktere jsou vzdy jednoduche prikazy
        // nebo volani metod / control flow
        var context = useParentContext ? new GeneratorContext(stackLevel, parentContext) : parentContext;
        allocateSpaceForVariables(context);

        // Zpracujeme jednotlive statementy
        var statementProcessor = new StatementProcessor();
        for (var statement : blockScope.getStatements()) {
            statementProcessor.process(context, statement);
        }
    }

    public void allocateSpaceForVariables(GeneratorContext context) throws CompileException {
        // Musime zjistit vsechny promenne, ktere se deklaruji a inicializuji

        // Vybereme vsechny deklarace a inicializace a ulozime je do hashsetu -> tim eliminujeme
        // duplicitni identifikatory
        var identifiersWithDataType = blockScope.getStatements()
                .stream()
                .filter(statement -> statement instanceof VariableDeclarationStatement
                        || statement instanceof VariableInitializationStatement)
                .map(statement -> {
                    // teoreticky by bylo lepsi udelat interface ale to je moc prace
                    if (statement instanceof VariableInitializationStatement) {
                        return new ImmutablePair<>(
                                ((VariableInitializationStatement) statement).getIdentifier(),
                                ((VariableInitializationStatement) statement).getDataType()
                        );
                    }
                    return new ImmutablePair<>(
                            ((VariableDeclarationStatement) statement).getIdentifier(),
                            ((VariableDeclarationStatement) statement).getDataType());
                })
                .collect(Collectors.toSet()); // ulozime do setu

        // Nyni staci pro kazdy identifier promenne vytvorit v kontextu promennou
        for (var identifierWithDataType : identifiersWithDataType) {
            context.allocateVariable(identifierWithDataType.left, identifierWithDataType.right);
        }
    }
}
