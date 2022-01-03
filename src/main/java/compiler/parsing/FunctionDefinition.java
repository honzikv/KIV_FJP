package compiler.parsing;

import compiler.compiletime.utils.VariableUtils;
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
     * Velikost parametru a navratove hodnoty
     */
    private final int paramsWithReturnValSize;

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
        this.paramsWithReturnValSize = calculateParamsWithReturnValSize();
    }

    /**
     * Vypocte velikost parametru s navratovou hodnotou a vrati vysledek
     *
     * @return velikost - pokud je Integer.MAX_VALUE pak nejaky typ je invalidni a vyhodime exception
     */
    private int calculateParamsWithReturnValSize() {
        var totalSize = 0;
        for (var param : functionParameters) {
            totalSize += VariableUtils.getSizeOfNonThrow(param.getDataType());
        }
        // K totalSize pridame 0 pokud je navratova hodnota void nebo velikost navratove hodnoty a vratime
        return totalSize + (returnType == DataType.Void ? 0 : VariableUtils.getSizeOfNonThrow(returnType));
    }

}
