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
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SelectAndOrderTag extends UIComponentTag
{
	
	private String availableItems;
	private String selectedItems;
	private String sortable;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SelectAndOrderComponent selectdAndOrder = (SelectAndOrderComponent) component;
		
		if (availableItems != null && isValueReference(availableItems))
		{
			ValueBinding vb = app.createValueBinding(availableItems);
			component.setValueBinding("availableItems", vb);
		}

		if (selectedItems != null && isValueReference(selectedItems))
		{
			ValueBinding vb = app.createValueBinding(selectedItems);
			component.setValueBinding("selectedItems", vb);
		}
		
		if (sortable != null && isValueReference(sortable))
		{
			ValueBinding vb = app.createValueBinding(sortable);
			component.setValueBinding("sortable", vb);
		}
		else
		{
			selectdAndOrder.setSortable("true".compareToIgnoreCase(sortable) == 0);
		}
			
	}
	
	public String getComponentType()
	{
		return "SelectAndOrder";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getAvailableItems()
	{
		return availableItems;
	}

	public void setAvailableItems(String items)
	{
		this.availableItems = items;
	}

	public String getSelectedItems()
	{
		return selectedItems;
	}

	public void setSelectedItems(String selectedItems)
	{
		this.selectedItems = selectedItems;
	}

	public String getSortable()
	{
		return sortable;
	}

	public void setSortable(String sortable)
	{
		this.sortable = sortable;
	}

}
