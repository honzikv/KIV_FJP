package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.IfStatement;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.function.ReturnStatement;
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StatementProcessor implements IProcessor {

    private Statement statement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Zpracovani provedeme tak, ze zjistime typ z enumu a pretypujeme na danou implementaci
        switch (statement.getStatementType()) {
            case ReturnStatement -> new ReturnStatementProcessor((ReturnStatement) statement)
                    .process(context);
            // Assignment a inicializace jsou temer to same, krome toho ze inicializace navic "deklaruje", takze
            // to zpracovava stejna trida
            case VariableAssignment, VariableInitialization -> new VariableInitializationProcessor(
                    (VariableAssignmentStatement) statement)
                    .process(context);
            case VariableDeclaration -> new VariableDeclarationProcessor((VariableDeclarationStatement) statement)
                    .process(context);
            case BlockScope -> new BlockScopeProcessor((BlockScope) statement)
                    .process(context);
//            case WhileLoop, DoWhileLoop, ForLoop, ForEachLoop, RepeatUntilLoop -> new LoopProcessor(
//                    (ForStatement) statement)
//                    .process(context);
            case IfStatement -> new IfStatementProcessor((IfStatement) statement)
                    .process(context);
//            case FunctionCall -> new FunctionCallProcessor((FunctionCall) statement)
//                    .process(context);
//            case FunctionParameter -> new FunctionParameterProcessor((FunctionParameter) statement)
//                    .process(context);
        }
    }

    /**
     * Varianta pro NoArgs constructor
     */
    public void process(GeneratorContext context, Statement statement) throws CompileException {
        this.statement = statement;
        process(context);
    }
}
