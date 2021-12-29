package parser;

import compiler.parsing.statement.variable.VariableInitializationStatement;
import main.antlr4.grammar.CMMLexer;
import main.antlr4.grammar.CMMParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;
import parser.visitor.EntrypointVisitor;

public class VariableTests {

    private static CMMParser createParser(String input) {
        return new CMMParser(new CommonTokenStream(new CMMLexer(CharStreams.fromString(input))));
    }

    @Test
    public void testBooleanVariables() {
        var input = """
                bool iiintt = false;
                bool booool = false;
                """;

        var parser = createParser(input);
        var entryPoint = new EntrypointVisitor().visit(parser.entrypoint());

        var variables = entryPoint.getChildStatements();

        for (var variable : variables) {
            Assert.assertEquals((((VariableInitializationStatement) variable).getLiteralValue()), "false");
        }
    }

}
