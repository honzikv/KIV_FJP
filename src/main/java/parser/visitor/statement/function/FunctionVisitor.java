package parser.visitor.statement.function;

import compiler.parsing.statement.function.FunctionDeclaration;
import compiler.parsing.statement.function.FunctionParameter;
import compiler.parsing.DataType;
import java.util.ArrayList;
import main.antlr4.grammar.CMMParser;
import parser.visitor.BlockScopeVisitor;
import parser.visitor.CMMLevelAwareVisitor;

public class FunctionVisitor extends CMMLevelAwareVisitor<FunctionDeclaration> {
    public FunctionVisitor(long depth) {
        super(depth);
    }

    @Override
    public FunctionDeclaration visitFunctionDeclarationStatement(CMMParser.FunctionDeclarationStatementContext ctx) {
        return visitFunctionDeclaration(ctx.functionDeclaration());
    }

    @Override
    public FunctionDeclaration visitFunctionDeclaration(CMMParser.FunctionDeclarationContext ctx) {
        var returnType = DataType.convertStringTypeToDataType(ctx.functionDataTypes().getText());
        var identifier = ctx.IDENTIFIER().getText();

        var functionParameters = ctx.functionParameters() != null
                ? new FunctionParametersVisitor(depth).visit(ctx.functionParameters())
                : new ArrayList<FunctionParameter>();

        var blockScope = new BlockScopeVisitor(depth + 1).visit(ctx.functionBlockScope());

        return new FunctionDeclaration(depth, returnType, identifier, functionParameters, blockScope);
    }
}
