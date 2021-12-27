package compiler.pl0;

import lombok.Getter;

/**
 * Typ instrukce pro PL0
 */
public enum PL0InstructionType {
    LIT("LIT"),
    INT("INT"),
    OPR("OPR"),
    JMP("JMP"),
    JMC("JMC"),
    LOD("LOD"),
    STO("STO"),
    CAL("CAL"),
    RET("RET");

    @Getter()
    private final String value;

    PL0InstructionType(String value) {
        this.value = value;
    }

}
