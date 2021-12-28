package parser.visitor.statement.variable;

import compiler.language.statement.variable.VariableInitializationStatement;
import main.antlr4.grammar.CMMParser;
import parser.visitor.CMMLevelAwareVisitor;

public class ConstVariableInitializationVisitor extends CMMLevelAwareVisitor<VariableInitializationStatement> {


    public ConstVariableInitializationVisitor(long depth) {
        super(depth);
    }

    @Override
    public VariableInitializationStatement visitConstVariableInitialization(
            CMMParser.ConstVariableInitializationContext ctx) {
        return new VariableInitializationVisitor(depth, true)
                .visitVariableInitialization(ctx.variableInitialization());
    }

    @Override
    public VariableInitializationStatement visitConstVariableInitializationStatement(
            CMMParser.ConstVariableInitializationStatementContext ctx) {
        return visitConstVariableInitialization(ctx.constVariableInitialization());
    }
}
