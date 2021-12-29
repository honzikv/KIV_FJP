package compiler.language.statement.function;

import compiler.language.statement.BlockScope;
import compiler.language.statement.Statement;
import compiler.language.statement.StatementType;
import compiler.language.variable.DataType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Deklarace funkce
 */
@Getter
@ToString
public class FunctionDeclaration extends Statement {

    private final DataType returnType;

    private final String identifier;

    private final List<FunctionParameter> functionParameters;

    private final BlockScope blockScope; // bude typu FunctionBlockScope

    public FunctionDeclaration(long depthLevel,
                               DataType returnType,
                               String identifier,
                               List<FunctionParameter> functionParameters,
                               BlockScope blockScope) {
        super(StatementType.FunctionDeclaration, depthLevel);
        this.returnType = returnType;
        this.identifier = identifier;
        this.functionParameters = functionParameters;
        this.blockScope = blockScope;
    }


}
