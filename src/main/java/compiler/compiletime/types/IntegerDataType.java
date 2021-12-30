package compiler.compiletime.types;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IntegerDataType {

    public static void addOnStack(GeneratorContext context, int value) {
        context.appendInstruction(PL0InstructionType.LIT, 0, value);
    }
}
