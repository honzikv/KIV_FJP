import compiler.Arguments;
import compiler.Compiler;
import compiler.utils.CompileException;
import interpreter.PL0Inter;

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

            var compiler = new Compiler();
            compiler.run();
            
            if(Arguments.isInterpreter())
            {
            	PL0Inter inter = new PL0Inter(Arguments.getInstructions());
            	inter.debbug = true;
            	inter.simulatePL0();
            }
        }
        catch (CompileException ex)
        {
            System.err.println("There was an error during compilation: ");
            System.err.println(ex.getMessage());
        }
    }
}
