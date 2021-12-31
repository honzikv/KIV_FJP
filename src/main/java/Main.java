import compiler.Compiler;
import compiler.utils.CompileException;

/**
 * Entrypoint programu pri spusteni prekladu
 */
public class Main {

    /**
     * Pro precteni ze souboru musime presmerovat stdin a pro vystup musime presmerovat stdout
     *
     * @param args
     */
    public static void main(String[] args) throws CompileException {
        new Compiler()
                .run();
    }
}
