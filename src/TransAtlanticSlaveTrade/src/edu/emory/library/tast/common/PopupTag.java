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

public class PopupTag extends UIComponentTag
{
	
	private String htmlToDisplay = null;
	private String width;
	private String height;

	public String getComponentType()
	{
		return "Popup";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		PopupComponent popup = (PopupComponent) component;
		
		if (width != null && isValueReference(width))
		{
			ValueBinding vb = app.createValueBinding(width);
			popup.setValueBinding("width", vb);
		}
		else
		{
			popup.setWidth(Integer.parseInt(width));
		}
		
		if (height != null && isValueReference(height))
		{
			ValueBinding vb = app.createValueBinding(height);
			popup.setValueBinding("height", vb);
		}
		else
		{
			popup.setHeight(Integer.parseInt(height));
		}

	}

	public String getHtmlToDisplay()
	{
		return htmlToDisplay;
	}

	public void setHtmlToDisplay(String htmlToDisplay)
	{
		this.htmlToDisplay = htmlToDisplay;
	}

	public String getWidth()
	{
		return width;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

}
