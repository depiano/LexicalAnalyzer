package Token;

public class Token
{
	private String name;
	private String value;
	
	public Token(String name,String value)
	{
		this.name=name;
		this.value=value;
	}
	
	public Token(String name)
	{
		this.name=name;
		this.value=null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if(this.value==null)
			return "Token ("+this.name+")";
		return "Token ("+name+","+value+")";
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
