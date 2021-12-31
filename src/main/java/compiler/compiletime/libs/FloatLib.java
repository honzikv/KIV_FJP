package compiler.compiletime.libs;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;
import org.apache.commons.lang3.NotImplementedException;

/**
 * TODO struktura floatu?
 */
public class FloatLib {
    public static void addOnStack(GeneratorContext context, Float value) {
        throw new NotImplementedException("TODO implement this feature");
    }

    public static int sizeOf() {
        return 4;
    }

    public static void loadToVariable(GeneratorContext context, long variableAddress) {
        for (var i = 0; i < sizeOf(); i += 1) {
            context.addInstruction(PL0InstructionType.STO, 0, variableAddress + i);
        }

        context.decreaseStackPointer(sizeOf());
    }

    public static void loadFromVariable(GeneratorContext context, long variableAddress) {
        for (var i = 0; i < sizeOf(); i += 1) {
            context.addInstruction(PL0InstructionType.LOD, 0, variableAddress + i);
        }

        context.increaseStackPointer(sizeOf());
    }

}
