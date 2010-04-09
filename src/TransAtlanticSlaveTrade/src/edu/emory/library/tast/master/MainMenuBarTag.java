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

public class MainMenuBarTag extends UIComponentTag
{
	
	private String menuItems;
	private String activeSectionId;
	private String activePageId;

	public String getComponentType()
	{
		return "MainMenuBar";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		MainMenuBarComponent menu = (MainMenuBarComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (menuItems != null && isValueReference(menuItems))
		{
			ValueBinding vb = app.createValueBinding(menuItems);
			menu.setValueBinding("menuItems", vb);
		}
		
		if (activeSectionId != null && isValueReference(activeSectionId))
		{
			ValueBinding vb = app.createValueBinding(activeSectionId);
			menu.setValueBinding("activeSectionId", vb);
		}
		else
		{
			menu.setActiveSectionId(activeSectionId);
		}

		if (activePageId != null && isValueReference(activePageId))
		{
			ValueBinding vb = app.createValueBinding(activePageId);
			menu.setValueBinding("activePageId", vb);
		}
		else
		{
			menu.setActivePageId(activePageId);
		}

	}

	public String getActivePageId()
	{
		return activePageId;
	}

	public void setActivePageId(String activePageId)
	{
		this.activePageId = activePageId;
	}

	public String getActiveSectionId()
	{
		return activeSectionId;
	}

	public void setActiveSectionId(String activeSectionId)
	{
		this.activeSectionId = activeSectionId;
	}

	public String getMenuItems()
	{
		return menuItems;
	}

	public void setMenuItems(String menuItems)
	{
		this.menuItems = menuItems;
	}

}
