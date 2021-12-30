package compiler.parsing;

import compiler.parsing.DataType;
import compiler.parsing.statement.BlockScope;
import compiler.parsing.statement.function.FunctionParameter;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * Deklarace funkce
 */
@Getter
@ToString
public class FunctionDefinition {

    private final DataType returnType;

    private final String identifier;

    private final List<FunctionParameter> functionParameters;

    private final BlockScope blockScope; // bude typu FunctionBlockScope

    public FunctionDefinition(DataType returnType,
                              String identifier,
                              List<FunctionParameter> functionParameters,
                              BlockScope blockScope) {
        this.returnType = returnType;
        this.identifier = identifier;
        this.functionParameters = functionParameters;
        this.blockScope = blockScope;
    }

}
