package compiler.compiletime;

import compiler.utils.CompileException;

/**
 * Processor === trida, ktera zpracuje dany usek kodu
 */
public interface IProcessor {

    void process(GeneratorContext context) throws CompileException;

}
