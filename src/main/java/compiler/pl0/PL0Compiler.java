package compiler.pl0;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Objekt prekladace, ktery se vola z mainu a provede preklad ze vstupu
 */
public class PL0Compiler {

    private final InputStream in;

    private final OutputStream out;

    public PL0Compiler(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void compile() {
        System.out.println("Compiling");
    }
}
