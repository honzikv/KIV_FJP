package compiler.pl0;

import compiler.compiletime.GeneratorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Reprezentuje instrukci pro PL0
 */
@Getter
@Setter
public class PL0Instruction {

    /**
     * Typ instrukce
     */
    private PL0InstructionType instructionType;

    /**
     * Uroven na stacku
     */
    private long stackLevel;

    /**
     * Cislo instrukce
     */
    private long instructionNumber;

    /**
     * Adresa pro danou instrukci
     */
    private long instructionAddress;

    private PL0Instruction(PL0InstructionType instructionType, long stackLevel, long instructionNumber,
                           long instructionAddress) {
        this.instructionType = instructionType;
        this.stackLevel = stackLevel;
        this.instructionNumber = instructionNumber;
        this.instructionAddress = instructionAddress;
    }

    /**
     * Funkce, ktera automaticky inkrementuje aktualni instrukci kontextu, aby se nemuselo psat mimo konstruktor
     */
    public static PL0Instruction create(GeneratorContext generatorContext, PL0InstructionType instructionType,
                                        long stackLevel, long instructionAddress) {
        var instruction = new PL0Instruction(
                instructionType, stackLevel, generatorContext.getInstructionNumber(), instructionAddress);
        generatorContext.incrementInstructionNumber();
        return instruction;
    }

    /**
     * Metoda se pouzije pri tisknuti vysledneho prelozeneho kodu
     *
     * @return toString prikaz s newline pro PL0
     */
    @Override
    public String toString() {
        return instructionNumber + " " + instructionType + " " + stackLevel + " " + instructionAddress;
    }
}
