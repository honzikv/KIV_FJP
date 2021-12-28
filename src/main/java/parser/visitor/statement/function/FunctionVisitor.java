package parser.visitor.statement.function;

import compiler.language.statement.function.FunctionDeclaration;
import compiler.language.statement.function.FunctionParameter;
import compiler.language.variable.DataType;
import java.util.ArrayList;
import java.util.Locale;
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
        var returnType = DataType.valueOf(ctx.FUNCTION_DATA_TYPES().getText().toUpperCase(Locale.ROOT));
        var identifier = ctx.identifier().getText();

        var functionParameters = ctx.functionParameters() != null
                ? new FunctionParametersVisitor(depth).visit(ctx.functionParameters())
                : new ArrayList<FunctionParameter>();

        var blockScope = new BlockScopeVisitor(depth + 1).visit(ctx.blockScope());

        return new FunctionDeclaration(depth, returnType, identifier, functionParameters, blockScope);
    }
}
