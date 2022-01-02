package compiler.compiletime.utils;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;
import org.apache.commons.lang3.NotImplementedException;

/**
 * TODO struktura floatu?
 * +-, cela cast, desetinna cast ?
 */
public class FloatUtils {
    public static void addOnStack(GeneratorContext context, Float value) {
        throw new NotImplementedException("TODO implement this feature");
    }

    public static int sizeOf() {
        return 3;
    }

    public static void storeToStackAddress(GeneratorContext context, long address) {
        for (var i = 0; i < sizeOf(); i += 1) {
            context.addInstruction(PL0InstructionType.STO, 0, address + i);
        }
    }

    public static void storeToStackAddress(GeneratorContext context, long level, long address) {
        for (var i = 0; i < sizeOf(); i += 1) {
            context.addInstruction(PL0InstructionType.LOD, level, address);
        }
    }

    public static void loadFromStackAddress(GeneratorContext context, long address) {
        for (var i = 0; i < sizeOf(); i += 1) {
            context.addInstruction(PL0InstructionType.LOD, 0, address + i);
        }
    }

    public static void loadFromStackAddress(GeneratorContext context, long level, long address) {
        for (var i = 0; i < sizeOf(); i += 1) {
            context.addInstruction(PL0InstructionType.LOD, level, address + i);
        }
    }

}
