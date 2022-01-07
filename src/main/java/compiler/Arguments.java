package compiler;

import java.io.File;

import compiler.utils.Debug;
import lombok.Getter;

public class Arguments
{
	@Getter
	/**Jestli se ma provest po vytvoreni instrukci spustit PL0 interpretr*/
	private static boolean interpreter = false;
	
	@Getter
	/**Udava, jaky typ vstupu se ma provest
	 * false - DEFAULT - vstup je definovan pomoci textu
	 *               zadaneho do konzole
	 * true - FILE - vstupem je textovy soubor s kodem*/
	private static boolean inputType = false;
	
	@Getter
	/**Udava, jaky typ vystupu se ma provest
	 * false - DEFAULT - vystup je presmerovan do koznole
	 * true - FILE - vystupem je textovy soubor*/
	private static boolean outputType = false;
	
	@Getter
	private static String inputFileName;
	
	@Getter
	private static String outputFileName = "c--output";
	
	/**
	 * Provede kontrolu vstupniho indexu do pole retezcu. Ppodiva se,
	 * jestli existuje dalis retezec v poli.
	 * @param		index		vstupni index do pole
	 * @param		length		velikost pole retezcu
	 * @return					0, pokud je vse OK, jinak hodnoty vyssi
	 * 							nez jedna podle chyby
	 */
	private static int checkIndexValidity(int index, int length)
	{
		if(index+1 >= length)
		{
			//System.out.println("Missing parameter for subsequant information!");
			return 1;
		}
		return 0;
	}
	
	/**
	 * Kontrola, jestli predany retezec obsahuje typ formatu
	 * souboru .txt.
	 * @param		fileName		nazev souboru
	 * @return						0, pokud je vse OK, jinak hodnoty vyssi
	 * 								nez jedna podle chyby
	 */
	private static int checkFileTypeTXT(String fileName)
	{
		if(fileName!= null && !fileName.endsWith(".txt"))
		{
			//System.out.println("File is not in .txt format!");
			return 2;
		}
		return 0;
	}
	
	/**
	 * Vytiskne napodvedu pro ovladani programu za pomoci argumentu.
	 */
	private static void printHelp()
	{
		System.out.println("------------------------------------------------");
		System.out.println("List of valid arguments that program recognizes:");
		System.out.println("------------------------------------------------");
		System.out.println("-h,-H				Prints out list of all valid arguments for the program.");
		System.out.println("-p,-P				Sets the program for runnig PL0 interpreter at the end in the console.");
		System.out.println("-i,-I <inputfilename.txt>	Sets the TXT file for the input of the program.");
		System.out.println("-o,-O <outputfilename.txt>	Sets the program for puting the output to the desired file instead of the console.");
		System.out.println("				Output file is optional. If not described as a argument, default file name will be used.");
		System.out.println("-d,-D				Enabling Debug mode.");
		System.out.println("------------------------------------------------");
	}
	
	/**
	 * Pruchod vsech argumentu predanych programu a nastaveni
	 * konkretnich paramteru na prislusne hodnoty.
	 * @param		args		vstupni parametry programu predane
	 */
    public static int manageArguments(String [] args)
    {
    	for(int i = 0; i < args.length; i++)
    	{
    		String argument = args[i]; 
    		switch(argument)
    		{
    		case "-p":
    		case "-P":
    			interpreter = true;
    			break;
    		case "-i":
    		case "-I":
    			int res = checkIndexValidity(i, args.length);
    			if(res == 0)
    			{
    				String tmp = args[++i];
    				if(checkFileTypeTXT(tmp) == 0)
    				{
    					File f = new File(tmp);
    					if(f.exists())
    					{
    						inputFileName = tmp;
    					}
    					else
    					{
    						System.out.println("Input file does not exist!");
    						return 3;
    					}
    				}
    				else
    				{
    					System.out.println("Input file is not in TXT format!");
    					return 2;
    				}
    			}
    			else
    			{
    				System.out.println("Argument for input file is missing!");
    				return 1;
    			}
    			inputType = true;
    			break;
    		case "-o":
    		case "-O":
    			res = checkIndexValidity(i, args.length);
    			if(res == 0)
    			{
    				String tmp = args[i+1];
    				if(checkFileTypeTXT(tmp) == 0)
    				{
    					outputFileName = tmp;
    					i++;
    				}
    			}
    			outputType = true;
    			break;
    		case "-d":
    		case "-D":
    			Debug.UseDebug = true;
    			break;
    		case "-h":
    		case "-H":
    			printHelp();
    			return 4;
    		default:
    			System.out.println("Unknown program arguments, for help set the argument to '-h' or '-H'.");
    			return 5;
    		}
		}
    	return 0;
    }
}
