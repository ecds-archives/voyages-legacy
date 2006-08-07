package edu.emory.library.tast.ui.schema;

import edu.emory.library.tast.dm.attributes.Group;

public class GroupForDisplay extends SchemaElementForDisplay
{
	
	public static final int SORT_BY_NAME = 0;
	public static final int SORT_BY_LABEL = 1;
	
	private String attributesHTML;
	private String compoundAttributesHTML;
	private String proxiedAttributesHTML;
	
	public GroupForDisplay(Group group)
	{
		this.setId(group.getId());
		this.setName(group.getName());
		this.setUserLabel(group.getUserLabel());
		this.setDescription(group.getDescription());
	}
	
	public String getAttributesHTML()
	{
		return attributesHTML;
	}
	
	public void setAttributesHTML(String attributesHTML)
	{
		this.attributesHTML = attributesHTML;
	}
	
	public String getCompoundAttributesHTML()
	{
		return compoundAttributesHTML;
	}
	
	public void setCompoundAttributesHTML(String compoundAttributesHTML)
	{
		this.compoundAttributesHTML = compoundAttributesHTML;
	}
	
	public String getProxiedAttributesHTML()
	{
		return proxiedAttributesHTML;
	}
	
	public void setProxiedAttributesHTML(String proxiedAttributesHTML)
	{
		this.proxiedAttributesHTML = proxiedAttributesHTML;
	}

}
