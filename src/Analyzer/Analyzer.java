package Analyzer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Token.Token;
import Token.TokenConstants;

public class Analyzer
{
	private ArrayList<Token> symbolTable;
	private File fin;
	private char[] buffer;
	private int state;
	private char character;
	private String currentToken;
	private Map<String,String> privateKey;
	private int index;
	
	public Analyzer(File fin) throws IOException
	{
		this.fin=fin;
		this.index=-1;
		
		FileReader fr = new FileReader(this.fin);
	    BufferedReader br = new BufferedReader(fr);
	    this.buffer = new char[(int) this.fin.length()];
	    br.read(this.buffer);
	    br.close();
	  
		this.symbolTable=new ArrayList<Token>();
		this.privateKey=new HashMap<String,String>();
		
		this.privateKey.put("if",null);
		this.privateKey.put("then",null);
		this.privateKey.put("else",null);
		
	}
	
	public Token lexer() throws IOException
	{
		if(this.index==this.buffer.length-1)
			return null;
		
		this.state=0;
		this.currentToken="";
		
		Token tk=null;
		
		this.character=this.buffer[++this.index];
		this.currentToken=this.currentToken+this.character;
		
		
		while((tk=this.getRELOP())==null)
		{
			this.character=this.buffer[++this.index];
			this.currentToken=this.currentToken+this.character;
		}
		
		return tk;
	}
	
	
	public Token getRELOP() throws IOException
	{		
		switch(this.state)
		{
			case 0:
				{
					if(this.character=='<')
					{
						this.state=1;
					}
					else if(this.character=='=')
					{
						this.state=5;
					}
					else if(this.character=='>')
					{
						this.state=6;
					}
					else
					{
						this.state=9;
						return this.getIDorKEYWords();
					}
				}
				break;
			case 1:
				{
					if(this.character=='=')
						this.state=2;
					else if(this.character=='>')
						this.state=3;
					else
					{
						this.state=4;
						return this.getRELOP();
					}
				}
				break;
			case 2:
				return (new Token(TokenConstants.RELOP,TokenConstants.LE));
			case 3:
				return (new Token(TokenConstants.RELOP,TokenConstants.NE));
			case 4:
				{
					this.index-=1;
					return (new Token(TokenConstants.RELOP,TokenConstants.LT));
				}
			case 5:
					return (new Token(TokenConstants.RELOP,TokenConstants.EQ));
			case 6:
				{
					if(this.character=='=')
						this.state=7;
					else
					{
						this.state=8;
						return this.getRELOP();
					}
				}
				break;
			case 7:
				return (new Token(TokenConstants.RELOP,TokenConstants.GE));
			case 8:
				{
					this.index-=1;
					return (new Token(TokenConstants.RELOP,TokenConstants.GT));
				}
			default:
					return this.getIDorKEYWords();
		}
		return null;
	}
	
	public Token getIDorKEYWords() throws IOException
	{
		switch(this.state)
		{
			case 9:
				{
					if(Character.isAlphabetic(this.character))
						this.state=10;
					else
					{
						this.state=12;
						return this.getNumber();
					}
				}
				break;
			case 10:
				{
					if(!Character.isLetterOrDigit(this.character))
					{
						this.state=11;
						return this.getIDorKEYWords();
					}
				}
				break;
			case 11:
				{
					Token tk=null;
					
					if(!this.privateKey.containsKey(this.currentToken.substring(0,this.currentToken.length()-1)))
					{
						tk=new Token("ID",this.currentToken.substring(0,this.currentToken.length()-1));
						this.symbolTable.add(tk);
						this.privateKey.put(this.currentToken.substring(0,this.currentToken.length()-1),null);
					}
					else
						tk=new Token(this.currentToken.substring(0,this.currentToken.length()-1));

					this.index-=1;
					return tk;
				}
			default:
					return this.getNumber();
		}
		return null;
	}
	
	public Token getNumber() throws IOException
	{
		switch(this.state)
		{
			case 12:
				{
					if(Character.isDigit(this.character))
						this.state=13;
					else
					{
						this.state=22;
						return this.getSpace();
					}
				}
				break;
			case 13:
				{
					if(this.character=='.')
						this.state=14;
					else if(this.character=='E')
						this.state=16;
					else if(!Character.isDigit(this.character))
					{
						this.state=20;
						return this.getNumber();
					}
				}
				break;
			case 14:
				{
					if(Character.isDigit(this.character))
						this.state=15;
					else
					{
						this.state=22;
						return this.getSpace();
					}
				}
				break;
			case 15:
				{
					if(this.character=='E')
						this.state=16;
					else if(!Character.isDigit(this.character))
					{
						this.state=21;
						return this.getNumber();
					}
				}
				break;
			case 16:
				{
					if(this.character=='+' || this.character=='-')
						this.state=17;
					else if(Character.isDigit(this.character))
						this.state=18;
					else
					{
						this.state=22;
						return this.getSpace();
					}					
				}
				break;
			case 17:
				{
					if(Character.isDigit(this.character))
						this.state=18;
					else
					{
						this.state=22;
						return this.getSpace();
					}
				}
				break;
			case 18:
				{
					if(!Character.isDigit(this.character))
					{
						this.state=19;
						return this.getNumber();
					}
				}
				break;
			case 20:
			case 21:
			case 19:
				{
					this.index-=1;
					Token tk=new Token(TokenConstants.NUMBER,this.currentToken.substring(0,this.currentToken.length()-1));
					this.symbolTable.add(tk);
					return tk;
				}
		default:
				return this.getSpace();
		}
		return null;
	}

	public Token getSpace() throws IOException
	{
		switch(this.state)
		{
			case 22:
				{
					if(this.character==' ')
					{
						this.state=23;
					}
					else
						this.state=25;
				}
				break;
			case 23:
				{
					if(this.character!=' ')
					{
						this.state=24;
						return this.getSpace();
					}
				}
				break;
			case 24:
				{
					this.index-=1;
					this.currentToken="";
					this.state=0;
				}
				break;
			default:
				System.out.println("This TOKEN \""+this.currentToken+"\" is not allowed!");
				Token tk=new Token(TokenConstants.ERROR,this.currentToken);
				return tk;
		}
		return null;
	}
	
	public void getSymbolTable()
	{
		for(int i=0;i<this.symbolTable.size();i++)
		{
			System.out.println("name:"+this.symbolTable.get(i).getName()+" ; value:"+this.symbolTable.get(i).getValue());
		}
	}
}
