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

    public static void storeToAddress(GeneratorContext context, DataType dataType, long level, long address)
            throws CompileException {
        switch (dataType) {
            case Int -> IntegerUtils.storeToStackAddress(context, level, address);
            case Boolean -> BooleanUtils.storeToStackAddress(context, level, address);
            case Float -> FloatUtils.storeToStackAddress(context, level, address);
            default -> throw new CompileException("Error cannot store unknown type to given level and address on stack!");
        }
    }

    /**
     * Nacte data ulozena na stacku do promenne
     *
     * @param context  kontext
     * @param variable promenna, kam se data ulozi
     * @throws CompileException chyba datoveho typu
     */
    public static void storeToVariable(GeneratorContext context, Variable variable) throws CompileException {
        switch (variable.getDataType()) {
            case Int -> IntegerUtils.storeToStackAddress(context, variable.getAddress());
            case Boolean -> BooleanUtils.storeToStackAddress(context, variable.getAddress());
            case Float -> FloatUtils.storeToStackAddress(context, variable.getAddress());
            default -> throw new CompileException("Error while reading variable from stack during expression assignment");
        }
    }

    /**
     * Nacte data do promenne z daneho levelu a adresy - zkratka pro instrukce LOD a STO
     *
     * @param context  kontext
     * @param variable promenna, do ktere se hodnoty nactou
     * @param level    uroven stacku
     * @param address  adresa ve stacku
     */
    public static void storeToVariable(GeneratorContext context, Variable variable, long level, long address) {
        switch (variable.getDataType()) {
            case Int -> {
                IntegerUtils.loadFromStackAddress(context, level, address);
                IntegerUtils.storeToStackAddress(context, variable.getStackLevel(), variable.getAddress());
            }
            case Float -> {
                FloatUtils.loadFromStackAddress(context, level, address);
                FloatUtils.storeToStackAddress(context, variable.getStackLevel(), variable.getAddress());
            }
            case Boolean -> {
                BooleanUtils.loadFromStackAddress(context, level, address);
                BooleanUtils.storeToStackAddress(context, variable.getStackLevel(), variable.getAddress());
            }
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
    public static void storeToVariable(GeneratorContext context, Variable from, Variable to)
            throws CompileException {
        switch (to.getDataType()) {
            case Int -> {
                IntegerUtils.loadFromStackAddress(context, from.getAddress());
                IntegerUtils.storeToStackAddress(context, to.getAddress());
            }
            case Boolean -> {
                BooleanUtils.loadFromStackAddress(context, from.getAddress());
                BooleanUtils.storeToStackAddress(context, to.getAddress());
            }
            case Float -> {
                FloatUtils.loadFromStackAddress(context, from.getAddress());
                FloatUtils.storeToStackAddress(context, to.getAddress());
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
