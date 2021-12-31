package parser.visitor.function;

import compiler.parsing.DataType;
import compiler.parsing.FunctionDefinition;
import compiler.parsing.statement.function.FunctionParameter;
import java.util.ArrayList;
import main.antlr4.grammar.CMMParser;
import parser.visitor.BlockScopeVisitor;
import parser.visitor.CMMLevelAwareVisitor;

public class FunctionDefinitionVisitor extends CMMLevelAwareVisitor<FunctionDefinition> {
    public FunctionDefinitionVisitor(long depth) {
        super(depth);
    }

    @Override
    public FunctionDefinition visitFunctionDefinition(CMMParser.FunctionDefinitionContext ctx) {
        var returnType = DataType.convertStringTypeToDataType(ctx.functionDataTypes().getText());
        var identifier = ctx.IDENTIFIER().getText();

        var functionParameters = ctx.functionParameters() != null
                ? new FunctionParametersVisitor(depth).visit(ctx.functionParameters())
                : new ArrayList<FunctionParameter>();

        var blockScope = new BlockScopeVisitor(depth + 1).visit(ctx.functionBlockScope());

        return new FunctionDefinition(returnType, identifier, functionParameters, blockScope);
    }
}
