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

public class MenuTag extends UIComponentTag
{
	
	private String onMenuSelected;
	private String items;
	private String customSubmitFunction;

	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		MenuComponent menu = (MenuComponent) component;
		
		if (items != null && isValueReference(items))
		{
			ValueBinding vb = app.createValueBinding(items);
			component.setValueBinding("items", vb);
		}
		
		if (onMenuSelected != null && isValueReference(onMenuSelected))
		{
			MethodBinding mb = app.createMethodBinding(onMenuSelected, new Class[] {MenuItemSelectedEvent.class});
			menu.setOnMenuSelected(mb);
		}
		
		menu.setCustomSubmitFunction(customSubmitFunction);

	}

	public String getComponentType()
	{
		return null;
	}

	public String getRendererType()
	{
		return null;
	}
	
	public String getItems()
	{
		return items;
	}

	public void setItems(String items)
	{
		this.items = items;
	}

	public String getOnMenuSelected()
	{
		return onMenuSelected;
	}

	public void setOnMenuSelected(String onMenuSelected)
	{
		this.onMenuSelected = onMenuSelected;
	}

	public String getCustomSubmitFunction()
	{
		return customSubmitFunction;
	}

	public void setCustomSubmitFunction(String customSubmitFunction)
	{
		this.customSubmitFunction = customSubmitFunction;
	}

}
