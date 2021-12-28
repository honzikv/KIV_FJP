package parser.visitor.statement.function;

import compiler.language.statement.function.FunctionParameter;
import compiler.language.variable.DataType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class FunctionParametersVisitor extends CMMLevelAwareVisitor<List<FunctionParameter>> {

    public FunctionParametersVisitor(long depth) {
        super(depth);
    }

    /**
     * Metoda, abysme nemuseli delat dalsi tridu pro FunctionParameter
     * @param ctx
     * @return
     */
    private FunctionParameter getFunctionParameter(CMMParser.FunctionParameterContext ctx) {
        return new FunctionParameter(
                depth,
                DataType.valueOf(ctx.LEGAL_DATA_TYPES().getText().toUpperCase(Locale.ROOT)),
                ctx.identifier().getText()
        );
    }

    @Override
    public List<FunctionParameter> visitFunctionParameters(CMMParser.FunctionParametersContext ctx) {
        var result = new ArrayList<FunctionParameter>();
        // Ziskame prvni funkcni parametr
        result.add(getFunctionParameter(ctx.functionParameter()));

        // A rekurzivne volame tuto metodu znovu pokud neni functionParameters null
        // Neni to uplne setrne k pameti
        if (ctx.functionParameters() != null) {
            result.addAll(visitFunctionParameters(ctx.functionParameters()));
        }

        return result;
    }
}
