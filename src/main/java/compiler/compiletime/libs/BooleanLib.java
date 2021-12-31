package compiler.compiletime.libs;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;

public class BooleanLib {

    /**
     * Boolean -> true === 0, false !== 0
     *
     * @param context kontext pro pridani instrukce
     * @param value   boolean hodnota
     */
    public static void addOnStack(GeneratorContext context, Boolean value) {
        context.addInstruction(PL0InstructionType.LIT, 0, value ? 0 : 1);
        context.incrementStackPointer();
    }

    public static int sizeOf() {
        return 1;
    }

    public static void loadToVariable(GeneratorContext context, long variableAddress) {
        context.addInstruction(PL0InstructionType.STO, 0, variableAddress);
        context.decrementStackPointer();
    }

    public static void loadFromVariable(GeneratorContext context, long variableAddress) {
        context.addInstruction(PL0InstructionType.LOD, 0, variableAddress);
        context.incrementStackPointer();
    }
}
