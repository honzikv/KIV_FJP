package compiler.language.variable;

import lombok.Getter;

public enum DataType {
    Integer("INT"),
    Boolean("BOOLEAN"),
    String("STRING"),
    Float("FLOAT"),
    Void("VOID"), // pouzite pouze pro return type
    Invalid("INVALID");

    @Getter
    private final String value;

    DataType(String value) {
        this.value = value;
    }

    public static DataType fromString(String input) {
        return switch (input) {
            case "int" -> Integer;
            case "float" -> Float;
            case "boolean" -> Boolean;
            case "string" -> String;
            case "void" -> Void;
            default -> Invalid;
        };

        // TODO remove
    }
}
