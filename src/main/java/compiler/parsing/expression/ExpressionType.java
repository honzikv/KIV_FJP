package compiler.parsing.expression;

/**
 * Typ vyrazu - abychom nemuseli rozlisovat pomoci instanceof
 */
public enum ExpressionType {
    Identifier,
    Value,
    Binary,
    Unary,
    FunctionCall,
    InstanceOf
}
