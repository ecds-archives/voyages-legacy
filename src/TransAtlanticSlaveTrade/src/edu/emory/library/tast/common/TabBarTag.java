package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class TabBarTag extends UIComponentTag
{

	private String selectedTabId;
	private String onTabChanged;

	protected void setProperties(UIComponent component)
	{
		
		super.setProperties(component);
		
		TabBarComponent tabBar = (TabBarComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (selectedTabId != null && isValueReference(selectedTabId))
		{
			ValueBinding vb = app.createValueBinding(selectedTabId);
			tabBar.setValueBinding("selectedTabId", vb);
		}
		else
		{
			tabBar.setSelectedTabId(selectedTabId);
		}

		if (onTabChanged != null && isValueReference(onTabChanged))
		{
			MethodBinding mb = app.createMethodBinding(onTabChanged, new Class[] {TabChangedEvent.class});
			tabBar.setOnTabChanged(mb);
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

	public String getSelectedTabId()
	{
		return selectedTabId;
	}

	public void setSelectedTabId(String selectedTabId)
	{
		this.selectedTabId = selectedTabId;
	}

	public String getOnTabChanged()
	{
		return onTabChanged;
	}

	public void setOnTabChanged(String onTabChanged)
	{
		this.onTabChanged = onTabChanged;
	}
	
}