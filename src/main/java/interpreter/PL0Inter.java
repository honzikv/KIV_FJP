package interpreter;

import java.util.*;

import compiler.Arguments;
import compiler.pl0.PL0Instruction;
/**
 * PL0 interpreter for our  generated C-- code.
 *
 */
public class PL0Inter
{
    /**Zasobnik hodnot*/
	private Stack<Long> stack;
	
	/**Predesly a konkretni index instrukce*/
	private long curr, prev;
	
	/**Seznam vsech instrukci pro interpretr*/
    private List<PL0Instruction> instructions;
    
    /**Baze*/
    private long base;
    
    /**Vrchol zasobniku*/
    private long head;
    
    /**Hodnoty, pro spravne zavolani callbacku*/
    private long [] calValues;
    
    /**Ziskani odbjektu konzole pro postupny vypis instrukci*/
    private Scanner in = new Scanner(System.in);
    
    /**DEBUG mod*/
    public boolean debbug;
    
    /**
     * Zakladni konstruktor
     * @param		inst		instrukce PL0 ke zpracovani
     */
    public PL0Inter(List<PL0Instruction> inst)
    {
        stack = new Stack<Long>();
        base = 0;
        head = -1;
        debbug = false;
        calValues = new long[3];
        instructions = inst;
    }
    
    /**
     * Simulace PL0 kod se vsim vsudy, kde je
     * po kazde provedene instrukci zobrazena
     * nasledujici instrukce, stav zasobniku,
     * bazi a vrchol zasobniku.
     */
    public void simulatePL0()
    {
    	curr = 0;
    	prev = 0;
    	calValues = new long[] {0,0,-1};
    	System.out.println("------------START------------");
    	System.out.println("For next step press 'ENTER'!");
    	long overallInstructionsCount = 1;
    	boolean skip = Arguments.isImmidiateInter();
    	do
    	{
    		long tmp = inputInstruction(instructions.get((int)curr));
    		prev = curr; 
    		if(tmp != -1)
    		{
    			curr = tmp;
    		}
    		else
    		{
    			curr++;
    		}
    		if(debbug)
    		{
    			System.out.println("----------------------------");
    			System.out.println("RUNNED INSTRUCTIONS: " + overallInstructionsCount);
    			System.out.println("CURR INSTRUCTION: " + instructions.get((int)prev));
    			System.out.println("NEXT INSTRUCTION: " + instructions.get((int)curr));
    			System.out.println("INST: " + prev);
    			System.out.println("BASE: " + base);
    			System.out.println("HEAD: " + head);
    			System.out.println(stack.toString());
    			System.out.println("----------------------------");
    		}
    		if(!skip)
    		{
    			in.nextLine();    			
    		}
    		overallInstructionsCount++;
    	}while(curr != instructions.size() - 1);
    	System.out.println("------------DONE------------");
    }
    
    /**
     * Provedeni konkretni instrukce na zaklade
     * jejiho typu.
     * @param		ins		vstupni instrukce
     * @return				-1 pokud se nic vratit nema,
     * 						jinak konkretni hodnota pro zmenu
     * 						dalsi volane instrukce			
     */
    private long inputInstruction(PL0Instruction ins)
    {
        switch(ins.getInstructionType())
        {
            case JMP:return JMP(ins);
            case INT:INT(ins);
                       break;
            case LOD:LOD(ins);
                       break;
            case LIT:LIT(ins);
                       break;
            case OPR:OPR(ins);
                       break;
            case STO:STO(ins);
                       break;
            case JMC:return JMC(ins);
            case RET:return RET(ins);
            case CAL:return CAL(ins);
            default:System.err.println("PL0Inter - inputInstruction - wrong" +
            "type of instruction " + ins.getInstructionType());
                    break;
        }
        return -1;
    }
    
    /**
     * Nacte hodnotu adresy v zasobniku a vlozi ji na vrchol zabosniku.
     * @param		ins		vstupni instrukce
     */
    private void LOD(PL0Instruction ins)
    {
        long tmp = 0;
        long immer = ins.getStackLevel();
        long value = ins.getInstructionAddress();
        if(immer > 0)
        {
            long locBase = this.base;
            for(long i = 0;i < immer;i++)
            {
                locBase = stack.get((int)locBase);
            }
            tmp = stack.get((int)(locBase + value));
        }
        else
        {
            tmp = stack.get((int)(this.base + value));
        }
        stack.push(tmp);
        this.head++;
    }
    
    /**
     * Ulozi hodnotu na vrcholu zasobniku na adresu v zasobniku.
     * @param		ins		vstupni instrukce
     */
    private void STO(PL0Instruction ins)
    {
    	long immer = ins.getStackLevel();
        long value = ins.getInstructionAddress();
    	long tmp = stack.pop();
        if(immer > 0)
        {
            long locBase = this.base;
            for(long i = 0;i < immer;i++)
            {
                locBase = stack.get((int)locBase);
            }
            stack.set((int)(locBase + value), tmp);
        }
        else
        {
            stack.set((int)(this.base + value), tmp);
        }   
        this.head--;
    }
    
    /**
     * Ulozeni konkretni honodty na vrchol zasobniku.
     * @param		ins		vstupni instrukce
     */
    private void LIT(PL0Instruction ins)
    {
        long value = ins.getInstructionAddress();
    	stack.push(value);
        this.head++;
    }
    
    /**
     * Zvyssi nebo snizi vrchol zasobniku.
     * @param		ins		vstupni instrukce
     */
    private void INT(PL0Instruction ins)
    {
        long value = ins.getInstructionAddress();
        boolean del = false;
        if(value < 0)
        {
        	del = true;
        	value *= -1;
        }
        for(int i = 0; i < value; i++)
        {
        	if(del)
        	{
        		stack.pop();
        		this.head--;
        	}
        	else
        	{
        		if(this.head+1 >= stack.size())
        		{
        			stack.push(0L);
        		}
        		this.head++;
        	}
		}
    }
    
    /**
     * Pokud je na vrcholu zasobniku 0, skoci na
     * konkretni instrukci.
     * @param		ins		vstupni instrukce
     */
    private long JMC(PL0Instruction ins)
    {
        long value = ins.getInstructionAddress();
        long tmp = stack.pop();
        this.head--;
        if(tmp == 0)
        {
        	return value;
        }
        else
        {
        	return -1;
        }
    }
    
    /**
     * Skok na konkretni instrukci.
     * @param		ins		vstupni instrukce
     */
    private long JMP(PL0Instruction ins)
    {
    	return ins.getInstructionAddress();
    }
    
    /**
     * Navraceni scopu programu na zaklade hodnot
     * zasobniku.
     * @param		ins		vstupni instrukce
     */
    private long RET(PL0Instruction ins)
    {
    	long staticBase = stack.get((int)(this.base));
    	long dynamicBase = stack.get((int)(this.base+1));
    	long instruction = stack.get((int)(this.base+2));
    	long remCount = this.head - this.base;
    	System.out.println(remCount);
    	for(int i = 0; i < (remCount + 1); i++)
    	{
    		stack.pop();
    	}
    	this.head -= remCount + 1;
    	if(staticBase != dynamicBase)
    	{
    		this.base = dynamicBase;
    	}
    	else
    	{
    		this.base = staticBase;
    	}
    	return instruction;
    }
    
    /**
     * Vytvoreni noveho scopu a ulozeni na zasobnik
     * cislo instrukce, bazi a adresu pro zpetny navrat
     * po ukonceni scopu.
     * @param		ins		vstupni instrukce
     */
    private long CAL(PL0Instruction ins)
    {
    	long immer = ins.getStackLevel();
    	long staticBase = this.base;
    	if(immer > 0)
        {
            staticBase = this.base;
            for(long i = 0;i < immer;i++)
            {
                staticBase = stack.get((int)staticBase);
            }
        }
    	long value = ins.getInstructionAddress();
    	calValues[0] = staticBase;
    	calValues[1] = this.base;
    	calValues[2] = ins.getInstructionNumber() + 1;
    	this.base = this.head + 1;
    	for(long val: calValues)
    	{
    		stack.push(val);
    	}
    	return value;
    }
    
    /**
     * Operace nad dvemi hodnotami na zasobniku,
     * polde predane hodnty na OPR.
     * @param		ins		vstupni instrukce
     */
    private void OPR(PL0Instruction ins)
    {
        long value = ins.getInstructionAddress();
    	long tmp1, tmp2;
        switch((int)value)
        {
            case 0://Kdysi pro operaci return
                   break;
            case 1://Negace hodnoty(prevracena hodnota)
                   tmp1 = stack.pop();
                   stack.push(Operations.NEG(tmp1));
                   break;
            case 2://Secteni dvou hodnot
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push(tmp1+tmp2);
                   this.head--;
                   break;
            case 3://Odecteni dvou hodnot
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push(tmp1-tmp2);
                   this.head--;
                   break;
            case 4://Nasobeni dvou hodnot
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push(tmp1*tmp2);
                   this.head--;
                   break;
            case 5://Deleni dvou cisel
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push(tmp1/tmp2);
                   this.head--;
                   break;
            case 6://Liche cislo(delitelne dvema)?
                   tmp1 = stack.pop();
                   if(tmp1%2 == 1)
                   {
                	   stack.push(1L);
                   }
                   else
                   {
                	   stack.push(0L);   
                   }
                   this.head--;
                   break;
            case 7://Modulo dvou cisel
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push(tmp1%tmp2);
                   this.head--;
                   break;
            case 8://==
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   if(tmp1 == tmp2)
                   {
                	   stack.push(1L);                	   
                   }
                   else
                   {
                	   stack.push(0L);
                   }
                   this.head--;
                   break;
            case 9://!=
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   if(tmp1 == tmp2)
                   {
                	   stack.push(0L);                	   
                   }
                   else
                   {
                	   stack.push(1L);
                   }
                   this.head--;
                   break;
            case 10://<
            		tmp1 = stack.pop();
            		tmp2 = stack.pop();
            		if(tmp1 < tmp2)
            		{
            			stack.push(1L);                	   
            		}
            		else
            		{
            			stack.push(0L);
            		}
            		this.head--;
                    break;
            case 11://<=
	            	tmp1 = stack.pop();
	        		tmp2 = stack.pop();
	        		if(tmp1 <= tmp2)
	        		{
	        			stack.push(1L);                	   
	        		}
	        		else
	        		{
	        			stack.push(0L);
	        		}
	        		this.head--;
                    break;
            case 12://>
	            	tmp1 = stack.pop();
	        		tmp2 = stack.pop();
	        		if(tmp1 > tmp2)
	        		{
	        			stack.push(1L);                	   
	        		}
	        		else
	        		{
	        			stack.push(0L);
	        		}
	        		this.head--;
	        		break;
            case 13://>=
	            	tmp1 = stack.pop();
	        		tmp2 = stack.pop();
	        		if(tmp1 >= tmp2)
	        		{
	        			stack.push(1L);                	   
	        		}
	        		else
	        		{
	        			stack.push(0L);
	        		}
	        		this.head--;
	        		break;
            default:System.out.println("PL0Interpreter - OPR - unkown instruction " + value);
                    break;
        }
    }
    
}
