package edu.emory.library.tas.web;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class SectionComponent extends UIComponentBase
{
	
	private String title;
	private String sectionId;
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = sectionId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		sectionId = (String) values[2];
	}

	public String getFamily()
	{
		return null;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String text)
	{
		this.title = text;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

}
