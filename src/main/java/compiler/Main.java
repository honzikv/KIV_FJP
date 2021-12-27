package compiler;

import compiler.pl0.PL0Compiler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Entrypoint programu pri spusteni prekladu
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Vystupni soubor - soubor a nebo stdout pokud neni druhy argument
            var outputFile = args.length == 2 ? new FileOutputStream(args[1]) : System.out;

            // Vstupni soubor - stdin pokud neni zadny argument a nebo soubor pokud je alespon jeden argument
            var inputFile = args.length >= 1 ? new FileInputStream(args[0]) : System.in;

            new PL0Compiler(inputFile, outputFile).compile();
        }
        catch (FileNotFoundException ex) {
            System.err.println("Error, specified input/output file does not exist");
        }
    }
}
