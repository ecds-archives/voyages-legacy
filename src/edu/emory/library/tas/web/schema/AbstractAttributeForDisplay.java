package edu.emory.library.tas.web.schema;

public class AbstractAttributeForDisplay extends SchemaElementForDisplay
{

	private String typeDisplayName;
	private String groupsHTML;

	public String getTypeDisplayName()
	{
		return typeDisplayName;
	}
	
	public void setTypeDisplayName(String typeDisplayName)
	{
		this.typeDisplayName = typeDisplayName;
	}

	public String getGroupsHTML()
	{
		return groupsHTML;
	}

	public void setGroupsHTML(String groupsHTML)
	{
		this.groupsHTML = groupsHTML;
	}

}
