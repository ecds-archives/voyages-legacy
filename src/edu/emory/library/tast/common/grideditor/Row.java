package edu.emory.library.tast.common.grideditor;

public class Row
{
	
	private String type;
	private String name;
	private String label;
	private String description;

	public Row(String type, String name, String label, String description)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.description = description;
	}

	public Row(String type, String name, String label)
	{
		this.type = type;
		this.name = name;
		this.label = label;
	}

	public String getDescription()
	{
		return description;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getType()
	{
		return type;
	}

}
