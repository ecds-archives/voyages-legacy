package edu.emory.library.tast.common.grideditor;


public class RowGroup
{
	
	private String label;
	private String name;
	
	public RowGroup(String name, String label)
	{
		this.name = name;
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
	
	public String getName()
	{
		return name;
	}

}