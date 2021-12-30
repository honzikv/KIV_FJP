import compiler.Compiler;

/**
 * Entrypoint programu pri spusteni prekladu
 */
public class Main {

    /**
     * Pro precteni ze souboru musime presmerovat stdin a pro vystup musime presmerovat stdout
     * @param args
     */
    public static void main(String[] args) {
        new Compiler()
                .run();
    }
}
