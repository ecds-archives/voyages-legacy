package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;

public class TabBarTag extends UIComponentTag
{

	private String tabChanged;

	protected void setProperties(UIComponent component)
	{
		
		TabBarComponent tabBar = (TabBarComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (tabChanged != null && isValueReference(tabChanged))
		{
			MethodBinding mb = app.createMethodBinding(tabChanged, new Class[] {TabChangeEvent.class});
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

	public String getTabChanged()
	{
		return tabChanged;
	}

	public void setTabChanged(String tabChanged)
	{
		this.tabChanged = tabChanged;
	}
	
}
