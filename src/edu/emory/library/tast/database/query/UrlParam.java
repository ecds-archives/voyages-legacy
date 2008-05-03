package edu.emory.library.tast.database.query;

public class UrlParam
{
	
	private String name;
	private String value;
	
	public UrlParam(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}
	
	public String getValue()
	{
		return value;
	}

}
