package edu.emory.library.tas.web;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

public class SectionTag extends UIComponentTag
{
	
	private String title;
	private String sectionId;

	public String getComponentType()
	{
		return "Section";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		SectionComponent section = (SectionComponent) component;

		section.setTitle(title);
		section.setSectionId(sectionId);
		
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
