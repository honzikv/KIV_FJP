package parser.visitor.variable;

import main.antlr4.grammar.CMMBaseVisitor;
import main.antlr4.grammar.CMMParser;

public class ChainAssignmentVisitor extends CMMBaseVisitor<String> {

    @Override
    public String visitChainAssignment(CMMParser.ChainAssignmentContext ctx) {
        return ctx.IDENTIFIER().getText();
    }
}
