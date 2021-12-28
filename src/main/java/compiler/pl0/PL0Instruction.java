package compiler.pl0;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Reprezentuje instrukci pro PL0
 */
@AllArgsConstructor
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

    /**
     * Metoda se pouzije pri tisknuti vysledneho prelozeneho kodu
     * @return toString prikaz s newline pro PL0
     */
    @Override
    public String toString() {
        return instructionNumber + " " + instructionType + " " + stackLevel + " " + instructionAddress;
    }
}
