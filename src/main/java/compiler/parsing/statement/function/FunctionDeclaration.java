package compiler.parsing.statement.function;

import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import compiler.parsing.DataType;
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
