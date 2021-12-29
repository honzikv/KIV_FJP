package compiler.parsing.expression;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class IdentifierExpression extends Expression {

    private final String identifier;

    public IdentifierExpression(String identifier) {
        super();
        this.identifier = identifier;
    }
}
