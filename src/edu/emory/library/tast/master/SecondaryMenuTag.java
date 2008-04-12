package edu.emory.library.tast.master;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SecondaryMenuTag extends UIComponentTag
{
	
	private String activeItemId;
	
	public String getComponentType()
	{
		return "SecondaryMenu";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SecondaryMenuComponent menu = (SecondaryMenuComponent) component;
		
		if (activeItemId != null && isValueReference(activeItemId))
		{
			ValueBinding vb = app.createValueBinding(activeItemId);
			menu.setValueBinding("activeItemId", vb);
		}
		else
		{
			menu.setActiveMenuItemId(activeItemId);
		}
		
	}
	
	public String getActiveItemId()
	{
		return activeItemId;
	}

	public void setActiveItemId(String activeItemId)
	{
		this.activeItemId = activeItemId;
	}


}
