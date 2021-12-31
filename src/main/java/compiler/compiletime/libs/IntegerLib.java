package compiler.compiletime.libs;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IntegerLib {

    public static void addOnStack(GeneratorContext context, int value) {
        context.addInstruction(PL0InstructionType.LIT, 0, value);
    }

    public static void loadToVariable(GeneratorContext context, long variableAddress) {
        context.addInstruction(PL0InstructionType.STO, 0, variableAddress);
    }

    public static int sizeOf() {
        return 1;
    }

}
