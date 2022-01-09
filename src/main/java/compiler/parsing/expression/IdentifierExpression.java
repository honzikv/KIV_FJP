package compiler.parsing.expression;

import lombok.Getter;
import lombok.ToString;

/**
 * Vyraz obsahujici identifikator, napr. int a = x; bude mit identifier expression "x"
 */
@Getter
@ToString
public class IdentifierExpression extends Expression {

    private final String identifier;

    public IdentifierExpression(String identifier) {
        super(ExpressionType.Identifier);
        this.identifier = identifier;
    }

}
