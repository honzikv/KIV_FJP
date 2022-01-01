package compiler.parsing.statement.function;

import compiler.parsing.expression.Expression;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.StatementType;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FunctionCall extends Statement {

    private final String identifier;

    /**
     * Parametry funkce - obecne to muze byt jakykoliv vyraz
     */
    private final List<Expression> parameters;


    public FunctionCall(long depthLevel, String identifier, List<Expression> parameters) {
        super(StatementType.FunctionCall, depthLevel);
        this.identifier = identifier;
        this.parameters = parameters;
    }
}
