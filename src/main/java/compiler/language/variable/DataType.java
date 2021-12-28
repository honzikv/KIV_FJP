package compiler.language.variable;

public enum DataType {
    Integer("INT"),
    Boolean("BOOLEAN"),
    String("STRING"),
    Float("FLOAT"),
    Void("VOID"); // pouzite pouze pro return type

    private final String value;

    DataType(String value) {
        this.value = value;
    }
}
