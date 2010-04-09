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

public class CopyToClipboardButtonTag extends UIComponentTag
{
	
	private String text;
	private String data;

	public String getComponentType()
	{
		return "CopyToClipboardButton";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		CopyToClipboardButtonComponent button = (CopyToClipboardButtonComponent) component;
		
		if (text != null && isValueReference(text))
		{
			ValueBinding vb = app.createValueBinding(text);
			button.setValueBinding("text", vb);
		}
		else
		{
			button.setText(text);
		}
		
		if (data != null && isValueReference(data))
		{
			ValueBinding vb = app.createValueBinding(data);
			button.setValueBinding("data", vb);
		}
		else
		{
			button.setData(data);
		}

	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

}
