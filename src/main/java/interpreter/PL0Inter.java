package interpreter;

import java.util.*;

import compiler.pl0.PL0Instruction;
/**
 * PL0 interpreter for our  generated C-- code.
 *
 */
public class PL0Inter
{
    private Stack<Long> stack;
    private PL0Instruction [] instructions;
    private long base;
    private long head;
    private long [] calValues;
    private Scanner in = new Scanner(System.in);
    
    public boolean debbug;
    
    public PL0Inter(PL0Instruction [] inst)
    {
        stack = new Stack<Long>();
        base = 0;
        head = -1;
        debbug = false;
        calValues = new long[3];
        instructions = new PL0Instruction[inst.length];
        for(int i = 0; i < inst.length; i++)
        {
        	instructions[i] = inst[i];
		}
    }
    
    public void simulatePL0()
    {
    	long index = 0;
    	long prev = 0;
    	calValues = new long[] {0,0,-1};
    	do
    	{
    		long tmp = inputInstruction(instructions[(int)index]);
    		prev = index; 
    		if(tmp != -1)
    		{
    			index = tmp;
    		}
    		else
    		{
    			index++;
    		}
    		if(debbug)
    		{
    			System.out.println("----------------------------");
    			System.out.println("CURR INSTRUCTION: " + instructions[(int)prev].toString());
    			System.out.println("NEXT INSTRUCTION: " + instructions[(int)index].toString());
    			System.out.println("INST: " + prev);
    			System.out.println("BASE: " + base);
    			System.out.println("HEAD: " + head);
    			System.out.println(stack.toString());
    			System.out.println("----------------------------");
    		}
    		in.nextLine();
    	}while(index != instructions.length - 1);
    	System.out.println("------------DONE------------");
    }
    
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
    
    private void INT(PL0Instruction ins)
    {
        long value = ins.getInstructionAddress();
    	stack.push(calValues[0]);
        stack.push(calValues[1]);
        stack.push(calValues[2]);
        for(int i = 3; i < value;i++)
        {
            stack.push((long)0);
        }
        this.base = this.head + 1;
        this.head += value;
    }
    
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
    
    private long JMP(PL0Instruction ins)
    {
    	return ins.getInstructionAddress();
    }
    
    private long RET(PL0Instruction ins)
    {
    	long b = stack.get((int)(this.base));
    	long lb = stack.get((int)this.base+1);
    	long in = stack.get((int)this.base+2);
    	long remCount = this.head - this.base;
    	for(int i = 0; i < remCount + 1; i++)
    	{
    		stack.pop();
    	}
    	this.head -= remCount + 1;
    	if(b != lb)
    	{
    		this.base = lb;
    	}
    	else
    	{
    		this.base = b;
    	}
    	return in;
    }
    
    private long CAL(PL0Instruction ins)
    {
    	long insNum = ins.getInstructionNumber();
    	long immer = ins.getStackLevel();
        long value = ins.getInstructionAddress();
    	long stepBack = this.base;
    	for(long i = 0; i < immer; i++)
    	{
    		stepBack = stack.get((int)stepBack);
    	}
    	calValues[0] = stepBack;
    	calValues[1] = this.base;
    	calValues[2] = insNum + 1;
    	return value;
    }
    
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
                   stack.push((tmp1+tmp2)& 255);
                   this.head--;
                   break;
            case 3://Odecteni dvou hodnot
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push((tmp1-tmp2)& 255);
                   this.head--;
                   break;
            case 4://Nasobeni dvou hodnot
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push((tmp1*tmp2)& 255);
                   this.head--;
                   break;
            case 5://Deleni dvou cisel
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push((tmp1/tmp2)& 255);
                   this.head--;
                   break;
            case 6://Liche cislo(delitelne dvema)?
                   tmp1 = stack.pop();
                   if(tmp1%2 == 1)
                   {
                	   stack.push((long)1);
                   }
                   else
                   {
                	   stack.push((long)0);   
                   }
                   this.head--;
                   break;
            case 7://Modulo dvou cisel
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   stack.push((tmp1%tmp2)& 255);
                   this.head--;
                   break;
            case 8://==
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   if(tmp1 == tmp2)
                   {
                	   stack.push((long)1);                	   
                   }
                   else
                   {
                	   stack.push((long)0);
                   }
                   this.head--;
                   break;
            case 9://!=
                   tmp1 = stack.pop();
                   tmp2 = stack.pop();
                   if(tmp1 == tmp2)
                   {
                	   stack.push((long)0);                	   
                   }
                   else
                   {
                	   stack.push((long)1);
                   }
                   this.head--;
                   break;
            case 10://<
            		tmp1 = stack.pop();
            		tmp2 = stack.pop();
            		if(tmp1 < tmp2)
            		{
            			stack.push((long)1);                	   
            		}
            		else
            		{
            			stack.push((long)0);
            		}
            		this.head--;
                    break;
            case 11://<=
	            	tmp1 = stack.pop();
	        		tmp2 = stack.pop();
	        		if(tmp1 <= tmp2)
	        		{
	        			stack.push((long)1);                	   
	        		}
	        		else
	        		{
	        			stack.push((long)0);
	        		}
	        		this.head--;
                    break;
            case 12://>
	            	tmp1 = stack.pop();
	        		tmp2 = stack.pop();
	        		if(tmp1 > tmp2)
	        		{
	        			stack.push((long)1);                	   
	        		}
	        		else
	        		{
	        			stack.push((long)0);
	        		}
	        		this.head--;
	        		break;
            case 13://>=
	            	tmp1 = stack.pop();
	        		tmp2 = stack.pop();
	        		if(tmp1 >= tmp2)
	        		{
	        			stack.push((long)1);                	   
	        		}
	        		else
	        		{
	        			stack.push((long)0);
	        		}
	        		this.head--;
	        		break;
            default:System.out.println("PL0Interpreter - OPR - unkown instruction " + value);
                    break;
        }
    }
    
    private void LIT(PL0Instruction ins)
    {
        long value = ins.getInstructionAddress();
    	stack.push(value);
        this.head++;
    }
    
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
}
