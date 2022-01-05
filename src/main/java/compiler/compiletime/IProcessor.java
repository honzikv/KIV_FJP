package compiler.compiletime;

import compiler.utils.CompileException;

/**
 * Processor === trida, ktera zpracuje dany usek kodu a prelozi ho na instrukce
 * Idea je podobna jako u visitoru, akorat dany prvek rekurzivne prevedeme na PL0 kod
 */
public interface IProcessor {

    /**
     * Kazdy procesor ma metodu pro zpracovani daneho objektu, ktera se mu ulozi
     * do fieldu
     *
     * @param context kontext
     * @throws CompileException chyba pri prekladu
     */
    void process(GeneratorContext context) throws CompileException;

}
