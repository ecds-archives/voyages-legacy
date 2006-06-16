package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;

public class TabBarTag extends UIComponentTag
{

	private String onTabChanged;

	protected void setProperties(UIComponent component)
	{
		
		TabBarComponent tabBar = (TabBarComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (onTabChanged != null && isValueReference(onTabChanged))
		{
			MethodBinding mb = app.createMethodBinding(onTabChanged, new Class[] {TabChangeEvent.class});
			tabBar.setTabChanged(mb);
		}
		
	}
	
	public String getComponentType()
	{
		return "TabBar";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getOnTabChanged()
	{
		return onTabChanged;
	}

	public void setOnTabChanged(String tabChanged)
	{
		this.onTabChanged = tabChanged;
	}
	
}
