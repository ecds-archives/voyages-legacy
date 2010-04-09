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

public class SimpleListTag extends UIComponentTag
{
	
	private String elements;
	private String listStyle;

	public String getComponentType()
	{
		return "SimpleList";
	}

	public String getRendererType()
	{
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SimpleListComponent list = (SimpleListComponent) component;
		
		if (elements != null && isValueReference(elements))
		{
			ValueBinding vb = app.createValueBinding(elements);
			list.setValueBinding("elements", vb);
		}

		if (listStyle != null && isValueReference(listStyle))
		{
			ValueBinding vb = app.createValueBinding(listStyle);
			list.setValueBinding("listStyle", vb);
		}
		else
		{
			list.setListStyle(SimpleListStyle.parse(listStyle));
		}

	}

	public String getElements()
	{
		return elements;
	}

	public void setElements(String elements)
	{
		this.elements = elements;
	}

	public String getListStyle()
	{
		return listStyle;
	}

	public void setListStyle(String listStyle)
	{
		this.listStyle = listStyle;
	}

}
