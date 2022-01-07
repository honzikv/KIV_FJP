import compiler.Arguments;
import compiler.Compiler;
import compiler.utils.CompileException;

/**
 * Entrypoint programu pri spusteni prekladu
 */
public class Main
{
	/**
     * Pro precteni ze souboru musime presmerovat stdin a pro vystup musime presmerovat stdout
     *
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
        	int res = Arguments.manageArguments(args);
            if(res > 0)
            {
            	return;
            }
        	// Debugovani vypisu
            //Debug.UseDebug = args.length > 0 && args[0].equals("--debug-mode");
            var compiler = new Compiler();
            compiler.run();
        }
        catch (CompileException ex)
        {
            System.err.println("There was an error during compilation: ");
            System.err.println(ex.getMessage());
        }
    }
}
