package compiler.compiletime.utils;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.Variable;
import compiler.parsing.DataType;
import compiler.utils.CompileException;

/**
 * Wrapper pro zjednoduseni prace s utils knihovnami pro dany typ
 * switch, pokud nas dany datovy typ nezajima
 */
public class VariableUtils {

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

    /**
     * Nacte data ulozena na stacku do promenne
     *
     * @param context  kontext
     * @param variable promenna, kam se data ulozi
     * @throws CompileException chyba datoveho typu
     */
    public static void loadToVariable(GeneratorContext context, Variable variable) throws CompileException {
        switch (variable.getDataType()) {
            case Int -> IntegerUtils.loadToVariable(context, variable.getAddress());
            case Boolean -> BooleanUtils.loadToVariable(context, variable.getAddress());
            case Float -> FloatUtils.loadToVariable(context, variable.getAddress());
            default -> throw new CompileException("Error while reading variable from stack during expression assignment");
        }
    }

    /**
     * Nacte data z jedne promenne do druhe
     *
     * @param context kontext
     * @param from    promenna, ze ktere se maji data nacist
     * @param to      promenna, do ktere se maji data ulozit
     * @throws CompileException chyba datoveho typu
     */
    public static void loadToVariable(GeneratorContext context, Variable from, Variable to)
            throws CompileException {
        switch (to.getDataType()) {
            case Int -> {
                IntegerUtils.loadFromVariable(context, from.getAddress());
                IntegerUtils.loadToVariable(context, to.getAddress());
            }
            case Boolean -> {
                BooleanUtils.loadFromVariable(context, from.getAddress());
                BooleanUtils.loadToVariable(context, to.getAddress());
            }
            case Float -> {
                FloatUtils.loadFromVariable(context, from.getAddress());
                FloatUtils.loadToVariable(context, to.getAddress());
            }
            default -> throw new CompileException("Error while reading " +
                    "variable from stack during expression assignment");
        }
    }

    public static int getSizeOf(DataType dataType) throws CompileException {
        return switch (dataType) {
            case Int -> IntegerUtils.sizeOf();
            case Boolean -> BooleanUtils.sizeOf();
            case Float -> FloatUtils.sizeOf();
            default -> throw new CompileException("Error while reading " +
                    "variable from stack during expression assignment");
        };
    }
}
