package edu.emory.library.tast.common.grideditor;

public class Row
{
	
	private String type;
	private String name;
	private String label;
	private String description;
	private String groupName;

	public Row(String type, String name, String label, String description, String groupName)
	{
		this.type = type;
		this.name = name;
		this.label = label;
		this.description = description;
		this.groupName = groupName;
	}

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

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

}
