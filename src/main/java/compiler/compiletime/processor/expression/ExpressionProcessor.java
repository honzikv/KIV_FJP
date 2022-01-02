package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.expression.BinaryOperationExpression;
import compiler.parsing.expression.Expression;
import compiler.parsing.expression.FunctionCallExpression;
import compiler.parsing.expression.IdentifierExpression;
import compiler.parsing.expression.UnaryOperationExpression;
import compiler.parsing.expression.ValueExpression;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Objekt pro zpracovani vyrazu. Rekurzivne resolvuje dilci vyrazy
 */
@AllArgsConstructor
@NoArgsConstructor // No args pro vice vyrazu najednou, abysme nevytvareli zbytecne objekty
public class ExpressionProcessor implements IProcessor {

    private Expression expression;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        switch (expression.getExpressionType()) {
            case Identifier -> new IdentifierExpressionProcessor((IdentifierExpression) expression)
                    .process(context);

            case Binary -> new BinaryOperationExpressionProcessor((BinaryOperationExpression) expression)
                    .process(context);

            case Value -> new ValueExpressionProcessor((ValueExpression) expression)
                    .process(context);

            case Unary -> new UnaryOperationExpressionProcessor((UnaryOperationExpression) expression)
                    .process(context);
            case FunctionCall -> new FunctionCallExpressionProcessor((FunctionCallExpression) expression)
                    .process(context);
        }
    }

    /**
     * Metoda pro NoArgsConstructor objekt - napr. pokud zpracovavame vice objektu najednou
     * @param context kontext, kam se instrukce ulozi
     * @param expression vyraz, ktery se bude evaluovat
     * @throws CompileException pokud dojde k chybe
     */
    public void process(GeneratorContext context, Expression expression) throws CompileException {
        this.expression = expression;
        process(context);
    }
}
