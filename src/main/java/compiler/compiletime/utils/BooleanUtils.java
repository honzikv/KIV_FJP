package compiler.compiletime.utils;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;

public class BooleanUtils {

    /**
     * Boolean -> true === cokoliv krome 0, false === 0
     *
     * @param context kontext pro pridani instrukce
     * @param value   boolean hodnota
     */
    public static void addOnStack(GeneratorContext context, Boolean value) {
        context.addInstruction(PL0InstructionType.LIT, 0, value ? 1 : 0);
    }

    public static int sizeOf() {
        return 1;
    }

    public static void storeToStackAddress(GeneratorContext context, long address) {
        context.addInstruction(PL0InstructionType.STO, 0, address);
    }

    public static void loadFromStackAddress(GeneratorContext context, long address) {
        context.addInstruction(PL0InstructionType.LOD, 0, address);
    }

    public static void loadFromStackAddress(GeneratorContext context, long level, long address) {
        context.addInstruction(PL0InstructionType.LOD, level, address);
    }

    public static void storeToStackAddress(GeneratorContext context, long level, long address) {
        context.addInstruction(PL0InstructionType.LOD, level, address);
    }
}
