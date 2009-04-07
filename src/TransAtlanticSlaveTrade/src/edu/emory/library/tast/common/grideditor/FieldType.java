package edu.emory.library.tast.common.grideditor;

public abstract class FieldType
{
	
	private String name;
	
	public FieldType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public abstract String getType();

}
