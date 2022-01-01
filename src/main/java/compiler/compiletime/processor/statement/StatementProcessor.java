package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.compiletime.processor.statement.variable.VariableAssignmentProcessor;
import compiler.compiletime.processor.statement.variable.VariableDeclarationProcessor;
import compiler.compiletime.processor.statement.variable.VariableInitializationProcessor;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.IfStatement;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.loop.ForLoopStatement;
import compiler.parsing.statement.loop.WhileLoopStatement;
import compiler.parsing.statement.variable.VariableAssignmentStatement;
import compiler.parsing.statement.variable.VariableDeclarationStatement;
import compiler.parsing.statement.variable.VariableInitializationStatement;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class StatementProcessor implements IProcessor {

    @Setter
    private Statement statement;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Zpracovani provedeme tak, ze zjistime typ z enumu a pretypujeme na danou implementaci
        // slo by to samozrejme udelat i pres reflexi nebo pomoci instanceof, ale Java z nejakeho duvodu nema instanceof
        // switch a enum bude pravdepodobne stejne rychlejsi
        switch (statement.getStatementType()) {
            case VariableAssignment -> new VariableAssignmentProcessor(
                    (VariableAssignmentStatement) statement)
                    .process(context);
            case VariableInitialization -> new VariableInitializationProcessor(
                    (VariableInitializationStatement) statement)
                    .process(context);
            case VariableDeclaration -> new VariableDeclarationProcessor((VariableDeclarationStatement) statement)
                    .process(context);
            case BlockScope -> {
                var blockScopeProcessor = new BlockScopeProcessor((BlockScope) statement, true,
                        context.getStackLevel(), context);
                blockScopeProcessor.allocateSpace();
                blockScopeProcessor.process(context);
            }

            case WhileLoop, DoWhileLoop -> new WhileLoopProcessor((WhileLoopStatement) statement)
                    .process(context);
            case ForLoop -> new ForLoopProcessor((ForLoopStatement) statement)
                    .process(context);
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
