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
package edu.emory.library.tast.master;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SecondaryMenuItemTag extends UIComponentTag
{
	
	private String menuId;
	private String label;
	private String href;
	private String expanded;

	public String getComponentType()
	{
		return "SecondaryMenuItem";
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SecondaryMenuItemComponent menu = (SecondaryMenuItemComponent) component;
		
		if (menuId != null && isValueReference(menuId))
		{
			ValueBinding vb = app.createValueBinding(menuId);
			menu.setValueBinding("menuId", vb);
		}
		else
		{
			menu.setMenuId(menuId);
		}

		if (label != null && isValueReference(label))
		{
			ValueBinding vb = app.createValueBinding(label);
			menu.setValueBinding("label", vb);
		}
		else
		{
			menu.setLabel(label);
		}
		
		if (href != null && isValueReference(href))
		{
			ValueBinding vb = app.createValueBinding(href);
			menu.setValueBinding("href", vb);
		}
		else
		{
			menu.setHref(href);
		}
		
		if (expanded != null && isValueReference(expanded))
		{
			ValueBinding vb = app.createValueBinding(expanded);
			menu.setValueBinding("expanded", vb);
		}
		else if (expanded != null)
		{
			menu.setExpanded(Boolean.parseBoolean(expanded));
		}

	}

	public String getRendererType()
	{
		return null;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getMenuId()
	{
		return menuId;
	}

	public void setMenuId(String menuId)
	{
		this.menuId = menuId;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getExpanded()
	{
		return expanded;
	}

	public void setExpanded(String expanded)
	{
		this.expanded = expanded;
	}

}
