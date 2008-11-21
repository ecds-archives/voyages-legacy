package edu.emory.library.tast.master;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SiteHeaderTag extends UIComponentTag
{
	
	private String activeSectionId;

	public String getComponentType()
	{
		return "SiteHeader";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		SiteHeaderComponent menu = (SiteHeaderComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (activeSectionId != null && isValueReference(activeSectionId))
		{
			ValueBinding vb = app.createValueBinding(activeSectionId);
			menu.setValueBinding("activeSectionId", vb);
		}
		else
		{
			menu.setActiveSectionId(activeSectionId);
		}

	}

	public String getActiveSectionId()
	{
		return activeSectionId;
	}

	public void setActiveSectionId(String activeSectionId)
	{
		this.activeSectionId = activeSectionId;
	}

}
