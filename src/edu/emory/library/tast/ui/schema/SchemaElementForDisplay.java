package edu.emory.library.tast.ui.schema;

public abstract class SchemaElementForDisplay
{
	
	private Long id;
	private String name;
	private String userLabel;
	private String description;

	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getUserLabel()
	{
		return userLabel;
	}
	
	public void setUserLabel(String userLabel)
	{
		this.userLabel = userLabel;
	}
	
}
