package compiler.compiletime.libs;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;

public class BooleanLib {

    /**
     * Boolean -> true === 0, false !== 0
     * @param context kontext pro pridani instrukce
     * @param value boolean hodnota
     */
    public static void addOnStack(GeneratorContext context, Boolean value) {
        context.appendInstruction(PL0InstructionType.LIT, 0, value ? 0 : 1);
    }

}
