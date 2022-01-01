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

    public static void loadToVariable(GeneratorContext context, long variableAddress) {
        context.addInstruction(PL0InstructionType.STO, 0, variableAddress);
    }

    public static void loadFromVariable(GeneratorContext context, long variableAddress) {
        context.addInstruction(PL0InstructionType.LOD, 0, variableAddress);
    }
}
