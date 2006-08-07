package edu.emory.library.tast.ui.schema;

import edu.emory.library.tast.dm.attributes.Attribute;

public class AttributeForDisplay extends AbstractAttributeForDisplay
{
	
	private String compoundAttributesHTML;
	private String proxiedGroupsHTML;
	
	public AttributeForDisplay(Attribute attribute)
	{
		this.setId(attribute.getId());
		this.setName(attribute.getName());
		this.setUserLabel(attribute.getUserLabel());
		this.setDescription(attribute.getDescription());
		this.setTypeDisplayName(attribute.getTypeDisplayName());
		this.setCategory(attribute.getCategory().intValue());
		this.setVisible(attribute.isVisible());
	}

	public String getCompoundAttributesHTML()
	{
		return compoundAttributesHTML;
	}
	
	public void setCompoundAttributesHTML(String compoundAttributesHTML)
	{
		this.compoundAttributesHTML = compoundAttributesHTML;
	}

	public String getProxiedGroupsHTML()
	{
		return proxiedGroupsHTML;
	}

	public void setProxiedGroupsHTML(String proxiedGroupsHTML)
	{
		this.proxiedGroupsHTML = proxiedGroupsHTML;
	}

}
