package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.DataType;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.loop.ForLoopStatement;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.utils.CompileException;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class BlockScopeProcessor implements IProcessor {

    private final BlockScope blockScope;

    @Getter
    private final GeneratorContext context;

    public BlockScopeProcessor(BlockScope blockScope, boolean useParentContext, int stackLevel, GeneratorContext context) {
        this.blockScope = blockScope;
        this.context = new GeneratorContext(stackLevel, context, useParentContext);
    }

    /**
     * Alokovani mista se provadi nekdy pred processingem - napr. pro cykly
     */
    public void allocateSpace() throws CompileException {
        allocateSpace(this.context);
    }

    /**
     * Overload pro Entrypoint
     *
     * @param context
     * @throws CompileException
     */
    public void allocateSpace(GeneratorContext context) throws CompileException {
        allocateSpaceForVariables(context);
        allocateSpaceForForLoops(context);
    }

    @Override
    public void process(GeneratorContext parentContext) throws CompileException {
        // Zpracujeme jednotlive statementy
        var statementProcessor = new StatementProcessor();
        for (var statement : blockScope.getStatements()) {
            statementProcessor.process(context, statement);
        }
    }

    private void allocateSpaceForVariables(GeneratorContext context) throws CompileException {
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

    private void allocateSpaceForForLoops(GeneratorContext context) throws CompileException {
        // Pro lepsi zapis bude for loop mit promennou automaticky alokovanou a bude vzdy integer
        var forLoops = blockScope.getStatements()
                .stream()
                .filter(statement -> statement instanceof ForLoopStatement)
                .map(loop -> (ForLoopStatement) loop)
                .collect(Collectors.toList());

        for (var loop : forLoops) {
            context.allocateVariable(loop.getIdentifier(), DataType.Int, true);
            context.getVariableOrDefault(loop.getIdentifier()).setDeclared(true); // nastavime za deklarovanou
        }

    }
}
