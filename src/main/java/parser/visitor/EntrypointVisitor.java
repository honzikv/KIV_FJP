package parser.visitor;

import compiler.language.Entrypoint;
import main.antlr4.grammar.CMMBaseVisitor;
import main.antlr4.grammar.CMMParser;
import parser.visitor.statement.ControlFlowVisitor;

/**
 * Visitor pro vstup programu - tzn. uplny zacatek derivacniho stromu.
 * Tento visitor se vola v compileru a provede celou analyzu parsovaciho stromu
 */
public class EntrypointVisitor extends CMMBaseVisitor<Entrypoint> {

    @Override
    public Entrypoint visitEntrypoint(CMMParser.EntrypointContext ctx) {
        var entrypoint = new Entrypoint();

        // Entrypoint obsahuje nula nebo vice statementu
        // Tzn kazdy statement resolvujeme
        ctx.statement().forEach(statement -> entrypoint.addStatement(new ControlFlowVisitor(0).visit(statement)));

        return entrypoint;
    }
}
