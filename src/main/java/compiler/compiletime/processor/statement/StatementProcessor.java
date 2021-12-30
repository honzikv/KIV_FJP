package compiler.compiletime.processor.statement;

import compiler.compiletime.GeneratorContext;
import compiler.compiletime.IProcessor;
import compiler.parsing.statement.Statement;
import compiler.parsing.statement.function.ReturnStatement;
import compiler.pl0.PL0Instruction;
import java.util.List;

public class StatementProcessor implements IProcessor {

    private final Statement statement;

    public StatementProcessor(Statement statement) {
        this.statement = statement;
    }


    @Override
    public void process(GeneratorContext context) {
        // Zpracovani provedeme tak, ze zjistime typ z enumu a pretypujeme na danou implementaci
        switch (statement.getStatementType()) {
            case ReturnStatement -> new ReturnStatementProcessor((ReturnStatement) statement)
                    .process(context);
        }
    }
}
