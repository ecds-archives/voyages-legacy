/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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