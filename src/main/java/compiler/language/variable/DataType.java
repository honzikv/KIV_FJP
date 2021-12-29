package compiler.language.variable;

import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

public enum DataType {
    Int("INT"),
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

    public static DataType convertStringTypeToDataType(String input) {
        return switch (input) {
            case "int" -> Int;
            case "float" -> Float;
            case "bool" -> Boolean;
            case "string" -> String;
            case "void" -> Void;
            default -> Invalid; // todo exception handling
        };

        // TODO remove
    }

    public static DataType getDataTypeFromValue(String value) {
        if (NumberUtils.isParsable(value)) {
            // Asi existujou 100% inteligentnejsi zpusoby, ale toto je malo kodu a rychle
            try {
                var val = NumberUtils.createInteger(value);
                return DataType.Int;
            }
            catch (NumberFormatException ex) {
                // Pokud nejde parsovat integer, tak je to float
                return DataType.Float;
            }
        }

        if (value.startsWith("\"") || value.startsWith("'")) {
            return DataType.String;
        }

        // Jinak to je boolean
        return DataType.Boolean;
    }
}
