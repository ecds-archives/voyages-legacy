package edu.emory.library.tast.common.grideditor;

public class Column
{
	
	private String label;
	private String name;
	private boolean readOnly = false;
	
	public Column(String name, String label)
	{
		this.name = name;
		this.label = label;
	}

	public Column(String name, String label, boolean readOnly)
	{
		this.name = name;
		this.label = label;
		this.readOnly = readOnly;
	}

	public String getLabel()
	{
		return label;
	}

	public String getName()
	{
		return name;
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}

}
