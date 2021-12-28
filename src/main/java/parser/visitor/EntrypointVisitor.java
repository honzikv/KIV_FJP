package parser.visitor;

import compiler.language.Entrypoint;
import main.antlr4.grammar.CMMBaseVisitor;
import main.antlr4.grammar.CMMParser;

/**
 * Visitor pro vstup programu - tzn. uplny zacatek derivacniho stromu
 */
public class EntrypointVisitor extends CMMBaseVisitor<Entrypoint> {

    @Override
    public Entrypoint visitEntrypoint(CMMParser.EntrypointContext ctx) {
        var entrypoint = new Entrypoint();

        // Entrypoint obsahuje nula nebo vice statementu
        // Tzn kazdy statement resolvujeme
        ctx.statement().forEach(statement -> entrypoint.addStatement(new StatementVisitor(0).visit(statement)));

        return entrypoint;
    }
}
