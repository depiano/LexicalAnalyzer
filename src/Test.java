import java.io.File;

import Analyzer.Analyzer;
import Token.Token;
import Token.TokenConstants;

public class Test {

	public static void main(String[] args)
	{
		try
		{
			File f=new File("dati.txt");
			if(f.exists())
			{
				System.out.println("Start...\n");
				Analyzer analyz=new Analyzer(f);
				
				Token tk=null;
				while((tk=analyz.lexer())!=null && tk.getName()!=TokenConstants.ERROR)
				{
					System.out.println(tk.toString());
				}
				System.out.println("Completed.");
				
				System.out.println("\n- Symbol Table -");
				analyz.getSymbolTable();
			}
			else
				System.out.println("Il file \"dati.txt\" non esiste!");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
