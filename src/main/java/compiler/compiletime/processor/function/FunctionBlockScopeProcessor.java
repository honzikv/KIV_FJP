package compiler.compiletime.processor.function;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.processor.expression.ExpressionProcessor;
import compiler.compiletime.processor.statement.BlockScopeProcessor;
import compiler.compiletime.processor.statement.StatementProcessor;
import compiler.parsing.DataType;
import compiler.parsing.FunctionDefinition;
import compiler.parsing.statement.function.FunctionBlockScope;
import compiler.utils.CompileException;

public class FunctionBlockScopeProcessor extends BlockScopeProcessor {

    private final FunctionBlockScope functionBlockScope;

    /**
     * Reference na deklaraci pro snazsi pouziti
     */
    private final FunctionDefinition functionDefinition;

    public FunctionBlockScopeProcessor(FunctionBlockScope blockScope, boolean useParentContext, int stackLevel,
                                       FunctionDefinition functionDefinition) {
        super(blockScope, useParentContext, stackLevel);
        this.functionBlockScope = blockScope;
        this.functionDefinition = functionDefinition;
    }

    @Override
    public void process(GeneratorContext context) throws CompileException {
        // Pro telo funkce novy kontext nema smysl vytvaret

        // Alokace mista
        super.allocateSpace(context);

        // Zpracovani statementu
        var statementProcessor = new StatementProcessor();
        for (var statement : functionBlockScope.getStatements()) {
            statementProcessor.process(context, statement);
        }

        var returnStatement = functionBlockScope.getReturnStatement();
        // Pokud je navratova hodnota void smazeme obsah scopu a vratime se
        if (functionDefinition.getReturnType() == DataType.Void) {
            if (returnStatement != null && returnStatement.getExpression() != null) {
                throw new CompileException("Error, return statement cannot return a value or an expression " +
                        "in a void function!");
            }
            deallocateSpace(context);
            return;
        }

        if (returnStatement == null) {
            throw new CompileException("Error, missing return statement!");
        }

        var expression = returnStatement.getExpression();

        // Vyraz nechame zpracovat a bude na vrcholu zasobniku
        var expressionProcessor = new ExpressionProcessor(expression);
        expressionProcessor.process(context);

        // Pokud ma vyraz jiny datovy typ vyhodime exception
        if (expression.getDataType() != functionDefinition.getReturnType()) {
            throw new CompileException("Error, returned value / expression type differs from expected return type!");
        }

        // Dealokujeme misto na stacku
        deallocateSpace(context);
    }
}
