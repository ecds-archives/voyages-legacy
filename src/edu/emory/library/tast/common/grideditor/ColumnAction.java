package edu.emory.library.tast.common.grideditor;

public class ColumnAction
{
	
	private String name;
	private String label;
	
	public ColumnAction(String name, String label)
	{
		this.name = name;
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

}
