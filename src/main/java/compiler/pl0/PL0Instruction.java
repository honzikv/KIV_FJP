package compiler.pl0;

import compiler.compiletime.GeneratorContext;
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

    public PL0Instruction(PL0InstructionType instructionType, long stackLevel, long instructionNumber,
                           long instructionAddress) {
        this.instructionType = instructionType;
        this.stackLevel = stackLevel;
        this.instructionNumber = instructionNumber;
        this.instructionAddress = instructionAddress;
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
