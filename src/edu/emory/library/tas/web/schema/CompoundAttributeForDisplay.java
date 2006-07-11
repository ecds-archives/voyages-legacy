package edu.emory.library.tas.web.schema;

import edu.emory.library.tas.attrGroups.CompoundAttribute;

public class CompoundAttributeForDisplay extends AbstractAttributeForDisplay
{
	
	private int attributesCount;
	private String attributesHTML;
	
	public CompoundAttributeForDisplay(CompoundAttribute attribute)
	{
		this.setId(attribute.getId());
		this.setName(attribute.getName());
		this.setUserLabel(attribute.getUserLabel());
		this.setDescription(attribute.getDescription());
		this.setTypeDisplayName(attribute.getTypeDisplayName());
		this.setAttributesCount(attribute.getAttributesCount());
		this.setCategory(attribute.getCategory().intValue());
		this.setVisible(attribute.isVisible());
	}

	public int getAttributesCount()
	{
		return attributesCount;
	}

	public void setAttributesCount(int attributesCount)
	{
		this.attributesCount = attributesCount;
	}

	public String getAttributesHTML()
	{
		return attributesHTML;
	}

	public void setAttributesHTML(String attributesHTML)
	{
		this.attributesHTML = attributesHTML;
	}

}
