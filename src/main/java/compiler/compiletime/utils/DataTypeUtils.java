package compiler.compiletime.utils;

import compiler.compiletime.GeneratorContext;
import compiler.parsing.DataType;
import compiler.utils.CompileException;

/**
 * Wrapper pro zjednoduseni prace s utils knihovnami pro dany typ
 * switch, pokud nas dany datovy typ nezajima
 */
public class DataTypeUtils {

    /**
     * Prida misto s default inicializaci na vrchol zasobniku
     */
    public static void addSpaceOnStack(GeneratorContext context, DataType dataType) throws CompileException {
        switch (dataType) {
            case Int -> IntegerUtils.addOnStack(context, 0);
            case Boolean -> BooleanUtils.addOnStack(context, false);
            case Float -> FloatUtils.addOnStack(context, .0f);
            default -> throw new CompileException("Error, invalid data type provided");
        }
    }
}
