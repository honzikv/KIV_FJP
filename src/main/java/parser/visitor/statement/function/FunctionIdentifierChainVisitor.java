package parser.visitor.statement.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class FunctionIdentifierChainVisitor extends CMMLevelAwareVisitor<List<String>> {
    public FunctionIdentifierChainVisitor(long depth) {
        super(depth);
    }


    @Override
    public List<String> visitIdentifierChain(CMMParser.IdentifierChainContext ctx) {
        var result = new ArrayList<>(List.of(ctx.identifier().getText()));

        if (ctx.identifierChain() != null) {
            result.addAll(visitIdentifierChain(ctx.identifierChain()));
        }

        return result;
    }


}
