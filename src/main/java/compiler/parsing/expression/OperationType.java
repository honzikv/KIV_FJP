package compiler.parsing.expression;

import lombok.Getter;

/**
 * Typ operace ve vyrazu
 */
public enum OperationType {
    Addition("+"),
    Subtraction("-"),
    Multiplication("*"),
    Division("/"),
    Modulo("%"),
    GreaterThan(">"),
    GreaterOrEqual(">="),
    Equal("=="),
    Lesser("<"),
    LesserOrEqual("<="),
    NotEqual("!="),
    BooleanAnd("&&"),
    BooleanOr("||"),
    BooleanNot("!"),
    UnaryPlus("+"),
    UnaryMinus("-");

    @Getter
    private final String stringValue;

    OperationType(String stringValue) {
        this.stringValue = stringValue;
    }

    public static OperationType getBinaryOperationType(String operation) {
        return switch (operation) {
            case "+" -> Addition;
            case "-" -> Subtraction;
            case "*" -> Multiplication;
            case "/" -> Division;
            case "%" -> Modulo;
            case ">" -> GreaterThan;
            case "<" -> Lesser;
            case ">=" -> GreaterOrEqual;
            case "<=" -> LesserOrEqual;
            case "==" -> Equal;
            case "!=" -> NotEqual;
            case "&&" -> BooleanAnd;
            case "||" -> BooleanOr;
            default -> UnaryMinus; // nestane se, pouze kvuli jave, protoze nemuzeme vyhodit exception
        };

    }

    public static OperationType getUnaryOperationType(String operation) {
        return switch (operation) {
            case "-" -> UnaryMinus;
            case "!" -> BooleanNot;
            default -> NotEqual; // nestane se, pouze kvuli jave
        };
    }
}
