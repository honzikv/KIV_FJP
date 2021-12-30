package compiler.compiletime.processor.expression;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.expression.IdentifierExpression;
import compiler.pl0.PL0InstructionType;
import compiler.utils.CompileException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IdentifierExpressionProcessor implements IProcessor {

    private final IdentifierExpression expression;

    @Override
    public void process(GeneratorContext context) throws CompileException {
        var identifier = expression.getIdentifier();

        // Ziskame promennou
        var variable = context.getVariableOrDefault(identifier);

        // Pokud symbol neexistuje vyhodime exceptionu, kterou compiler vypise
        if (variable == null) {
            throw new CompileException("Error, unknown symbol: " + identifier + " referenced before declaration.");
        }

        // A umistime ji do kontextu
        context.appendInstruction(PL0InstructionType.LIT, variable.getLevel(), variable.getAddress());

        // Nastavime ocekavany typ, abychom mohli kontrolovat vyse
        expression.setDataType(variable.getDataType());
    }
}
