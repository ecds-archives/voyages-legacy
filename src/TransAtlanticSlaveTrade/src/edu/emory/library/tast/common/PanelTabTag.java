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
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag for panel tabs.
 * The tag can have following attributes:
 * title - title visible on header of this tab
 * sectionId - id of this tab (used to recognize selected tab; see PanelTabSetTag for more information)
 *
 */
public class PanelTabTag extends UIComponentTag
{

	private String title;
	private String sectionId;
	private String href;
	private String cssClass;

	public String getComponentType()
	{
		return "PanelTab";
	}

	public String getRendererType()
	{
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		PanelTabComponent section = (PanelTabComponent) component;
		Application app = getFacesContext().getApplication();

		if (title != null && isValueReference(title))
		{
			ValueBinding vb = app.createValueBinding(title);
			component.setValueBinding("title", vb);
		}
		else
		{
			section.setTitle(title);
		}
		
		if (sectionId != null && isValueReference(sectionId))
		{
			ValueBinding vb = app.createValueBinding(sectionId);
			component.setValueBinding("sectionId", vb);
		}
		else
		{
			section.setSectionId(sectionId);
		}

		if (href != null && isValueReference(href))
		{
			ValueBinding vb = app.createValueBinding(href);
			component.setValueBinding("href", vb);
		}
		else
		{
			section.setHref(href);
		}
		
		if (cssClass != null && isValueReference(cssClass))
		{
			ValueBinding vb = app.createValueBinding(cssClass);
			component.setValueBinding("cssClass", vb);
		}
		else
		{
			section.setCssClass(cssClass);
		}
		
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getCssClass()
	{
		return cssClass;
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

}