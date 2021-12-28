package parser.visitor;

import lombok.AllArgsConstructor;
import main.antlr4.grammar.CMMBaseVisitor;

/**
 * Extenze slouzici k ulozeni hloubky v deklaraci podle {}
 * @param <T>
 */
@AllArgsConstructor
public abstract class CMMLevelAwareVisitor<T> extends CMMBaseVisitor<T> {

    protected long depth;
}
