package compiler.parsing;

import compiler.parsing.statement.function.FunctionBlockScope;
import compiler.parsing.statement.function.FunctionParameter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
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

    private final FunctionBlockScope blockScope; // bude typu FunctionBlockScope


    /**
     * Adresa na stacku
     */
    @Setter
    private long address;

    /**
     * Adresa parametru
     */
    @Setter
    private long paramsAddress;

    /**
     * Adresa navratove hodnoty
     */
    @Setter
    private long returnValueAddress;

    /**
     * Jmeno promenne pro navratovou hodnotu
     */
    @Setter
    private String returnIdentifier;

    public FunctionDefinition(DataType returnType,
                              String identifier,
                              List<FunctionParameter> functionParameters,
                              FunctionBlockScope blockScope) {
        this.returnType = returnType;
        this.identifier = identifier;
        this.functionParameters = functionParameters;
        this.blockScope = blockScope;
    }

}
