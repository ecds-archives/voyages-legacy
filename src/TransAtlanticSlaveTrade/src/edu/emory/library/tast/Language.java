package edu.emory.library.tast;


public class Language
{
	
	private String code;
	private String name;
	private boolean active;
	
	public Language(String code, String name, boolean active)
	{
		this.code = code;
		this.name = name;
		this.active = active;
	}

	public String getCode()
	{
		return code;
	}

	public String getName()
	{
		return name;
	}

	public boolean isActive()
	{
		return active;
	}

}