package compiler.language.expression;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class IdentifierExpression extends Expression {

    private final String identifier;

    protected IdentifierExpression(String identifier) {
        super();
        this.identifier = identifier;
    }
}
