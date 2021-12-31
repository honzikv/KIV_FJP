package compiler.compiletime.libs;

import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0InstructionType;
import java.nio.charset.StandardCharsets;

/**
 * Knihovna pro praci s retezci. Retezce jsou implementovane stejne jako v C - terminovane pomoci \0
 */
public class StringLib {

    private static final byte StringTermination = '\0';

    public static void addOnStack(GeneratorContext context, String value) {
        // Na zasobnik pridame vsechny znaky
        for (var symbol : value.getBytes(StandardCharsets.UTF_8)) {
            context.addInstruction(PL0InstructionType.LIT, 0, symbol);
        }
        // Zakoncime 0
        context.addInstruction(PL0InstructionType.LIT, 0, StringTermination);
    }

    public static int sizeOf(String string) {
        return string.length() + 1; // + 0 terminal
    }
}
