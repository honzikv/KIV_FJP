package parser.visitor;

import lombok.AllArgsConstructor;
import main.antlr4.grammar.CMMBaseVisitor;

@AllArgsConstructor
public abstract class CMMLevelAwareVisitor<T> extends CMMBaseVisitor<T> {

    protected long depth;
}
