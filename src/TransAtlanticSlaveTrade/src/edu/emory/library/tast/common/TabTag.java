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

public class TabTag extends UIComponentTag
{
	
	private String text;
	private String tabId;
	
	protected void setProperties(UIComponent component)
	{
		
		TabComponent tab = (TabComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (text != null && isValueReference(text))
		{
			ValueBinding vb = app.createValueBinding(text);
			tab.setValueBinding("text", vb);
		}
		else
		{
			tab.setText(text);
		}
		
		if (tabId != null && isValueReference(tabId))
		{
			ValueBinding vb = app.createValueBinding(tabId);
			tab.setValueBinding("tabId", vb);
		}
		else
		{
			tab.setTabId(tabId);
		}

	}

	public String getComponentType()
	{
		return "Tab";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getTabId()
	{
		return tabId;
	}

	public void setTabId(String tabId)
	{
		this.tabId = tabId;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

}