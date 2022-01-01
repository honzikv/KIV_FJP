package compiler.pl0;

import compiler.parsing.expression.OperationType;
import compiler.utils.CompileException;

/**
 * Utilitni funkce pro PL0
 */
public class PL0Utils {

    public static int getOperationNumberFromOperationType(OperationType operationType) throws CompileException {
        return switch (operationType) {
            case UnaryMinus -> 1;
            case Addition, BooleanOr -> 2; // OR == Logicky soucet
            case Subtraction -> 3;
            case Multiplication, BooleanAnd -> 4; // AND == Logicky soucin
            case Division -> 5;
            case Modulo -> 6;
            case Equal -> 8;
            case NotEqual -> 9;
            case Lesser -> 10;
            case GreaterOrEqual -> 11;
            case GreaterThan -> 12;
            case LesserOrEqual -> 13;
            // Debug
            default -> throw new CompileException("Invalid operationType provided");
        };
    }
}
