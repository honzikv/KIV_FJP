package compiler.compiletime.utils;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IntegerUtils {

    public static void addOnStack(GeneratorContext context, int value) {
        context.addInstruction(PL0InstructionType.LIT, 0, value);
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
        context.addInstruction(PL0InstructionType.STO, level, address);
    }

    public static int sizeOf() {
        return 1;
    }

}
