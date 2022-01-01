package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.utils.IntegerUtils;
import compiler.compiletime.utils.VariableUtils;
import compiler.parsing.DataType;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.loop.ForLoopStatement;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;

@AllArgsConstructor
public class BlockScopeProcessor implements IProcessor {

    private final BlockScope blockScope;

    private boolean useParentContext;

    private final int stackLevel;
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

        // Vytvorime child context pro tento blok
        var context = new GeneratorContext(stackLevel, parentContext, useParentContext);

        // Alokujeme misto pro promenne
        allocateSpace(context);

        // Zpracujeme jednotlive statementy
        var statementProcessor = new StatementProcessor();
        for (var statement : blockScope.getStatements()) {
            statementProcessor.process(context, statement);
        }

        // Dealokujeme prostor
        deallocateSpace(context);

    }

    private Set<ImmutablePair<String, DataType>> getIdentifiersWithDataType() {
        return blockScope.getStatements()
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
    }


    private void allocateSpaceForVariables(GeneratorContext context) throws CompileException {
        // Musime zjistit vsechny promenne, ktere se deklaruji a inicializuji

        // Vybereme vsechny deklarace a inicializace a ulozime je do hashsetu -> tim eliminujeme
        // duplicitni identifikatory
        var identifiersWithDataType = getIdentifiersWithDataType();

        // Nyni staci pro kazdy identifier promenne vytvorit v kontextu promennou
        for (var identifierWithDataType : identifiersWithDataType) {
            context.allocateVariable(identifierWithDataType.left, identifierWithDataType.right);
        }
    }

    private List<ForLoopStatement> getForLoops() {
        return blockScope.getStatements()
                .stream()
                .filter(statement -> statement instanceof ForLoopStatement)
                .map(loop -> (ForLoopStatement) loop)
                .collect(Collectors.toList());
    }

    private void allocateSpaceForForLoops(GeneratorContext context) throws CompileException {
        // Pro lepsi zapis bude for loop mit promennou automaticky alokovanou a bude vzdy integer
        var forLoops = getForLoops();

        for (var loop : forLoops) {
            context.allocateVariable(loop.getIdentifier(), DataType.Int, true);
            context.getVariableOrDefault(loop.getIdentifier()).setDeclared(true); // nastavime za deklarovanou
        }

    }

    /**
     * Provede dealokaci stacku, aby nedochazelo k memory leaku
     *
     * @param context kontext, kam se instrukce ulozi
     * @throws CompileException
     */
    public void deallocateSpace(GeneratorContext context) throws CompileException {
        var identifiersWithDataType = getIdentifiersWithDataType();
        var totalSize = 0;

        for (var pair : identifiersWithDataType) {
            totalSize += VariableUtils.getSizeOf(pair.right);
        }

        // For loop ma vzdy velikost intu
        var forLoops = getForLoops();

        totalSize += forLoops.size() * IntegerUtils.sizeOf();
        context.addInstruction(PL0InstructionType.INT, 0, -totalSize);
    }
}
