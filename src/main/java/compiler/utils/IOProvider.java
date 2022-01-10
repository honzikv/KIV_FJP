package compiler.utils;

import compiler.Arguments;
import compiler.pl0.PL0Instruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Wrapper pro zapis a cteni ze stdio
 */
public class IOProvider
{

    /**
     * Precte vse co je na stdinu
     * @return retezec obsahujici data
     */
    public static String read()
    {
        var stinScanner = new Scanner(System.in);

        var input = new ArrayList<String>();
        while (stinScanner.hasNext()) {
            input.add(stinScanner.nextLine());
        }

        stinScanner.close();
        return String.join("\n", input);
    }
    
    /**
     * Kompletni nacteni celeho souboru do jednoho retezce.
     * @param		fileName		nazev vstupniho souboru
     * @return
     */
    public String readFile(String fileName)
    {
    	String content = "";
    	InputStream in = getClass().getResourceAsStream("/" + fileName);
    	if(in == null)
    	{
    		return "error";
    	}
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	content = reader.lines().collect(Collectors.joining());

    	return content;
    }
    
    /**
     * Vytiskne instrukce na stdout
     * @param instructions seznam instrukci
     */
    public static void write(List<PL0Instruction> instructions)
    {
        instructions.forEach(System.out::println);
    }
    
    /**
     * Zapis vsech instrukci PL0 do souboru.
     * @param fileName
     * @param instructions
     */
    public static void writeToFile(String fileName, List<PL0Instruction> instructions)
    {
    	if(fileName == null || instructions == null)
    	{
    		return;
    	}
		
    	/*
		 * if(Arguments.isOutputType() && fileName.equals("c--output")) {
		 * SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); Date
		 * date = new Date(System.currentTimeMillis()); String currentTime =
		 * formatter.format(date); fileName = "c--output_" + currentTime + ".txt"; }
		 */
    	
    	File f = new File(fileName);
    	try
    	{
			f.createNewFile();
			FileWriter myWriter = new FileWriter(fileName);
			long count = 0;
			for(PL0Instruction instruction : instructions)
			{
				myWriter.write(instruction + "\n");
				count++;
			}
		    myWriter.close();
		}
    	catch(IOException e)
    	{
			e.printStackTrace();
		}
    }
}
