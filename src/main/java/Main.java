import compiler.Arguments;
import compiler.Compiler;
import compiler.compiletime.GeneratorContext;
import compiler.pl0.PL0Instruction;
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
		/*
		 * long k = 111; String resultWithPadding = String.format("%64s",
		 * Long.toBinaryString(k)).replaceAll(" ", "0"); // 32-bit Integer
		 * System.out.println(resultWithPadding); k = Operations.NEG(k);
		 * resultWithPadding = String.format("%64s",
		 * Long.toBinaryString(k)).replaceAll(" ", "0"); // 32-bit Integer
		 * System.out.println(resultWithPadding);
		 */
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
            
            if(Arguments.isInterpreter())
            {
            	PL0Inter inter = new PL0Inter((PL0Instruction[])Arguments.getInstructions().toArray());
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
