package compiler.compiletime;

import compiler.pl0.PL0Instruction;
import compiler.utils.CompileException;
import java.util.List;

/**
 * Interface, ktery umoznuje zpracovani daneho elementu kodu na instrukce
 */
public interface IProcessor {

    void process(GeneratorContext context) throws CompileException;

}
