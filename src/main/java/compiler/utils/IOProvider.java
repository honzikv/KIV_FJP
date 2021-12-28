package compiler.utils;

import compiler.pl0.PL0Instruction;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Wrapper pro zapis a cteni ze stdio
 */
public class IOProvider {

    /**
     * Precte vse co je na stdinu
     * @return retezec obsahujici data
     */
    public static String read() {
        var stinScanner = new Scanner(System.in);

        var input = new ArrayList<String>();
        while (stinScanner.hasNext()) {
            input.add(stinScanner.nextLine());
        }

        stinScanner.close();
        return String.join("\n", input);
    }

    /**
     * Vytiskne instrukce na stdout
     * @param instructions seznam instrukci
     */
    public static void write(List<PL0Instruction> instructions) {
        instructions.forEach(System.out::println);
    }
}
