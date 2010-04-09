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
package edu.emory.library.tast.reditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.StringUtils;

public class FieldValueText extends FieldValue
{
	
	public static final String TYPE = "text";

	private String value;

	public FieldValueText(String name)
	{
		super(name);
	}
	
	public FieldValueText(String name, String value)
	{
		super(name);
		this.value = value;
	}

	public static String getHtmlFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name;
	}

	public void decode(UIComponent component, FacesContext context)
	{
		String htmlFieldName = FieldValueText.getHtmlFieldName(component, context, getName());
		value = (String) context.getExternalContext().getRequestParameterMap().get(htmlFieldName);
	}

	public boolean isEmpty()
	{
		return StringUtils.isNullOrEmpty(value); 
	}
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String[] getLines()
	{
		if (value == null)
		{
			return new String[0];
		}
		else
		{
			return value.split("\n");
		}
	}

}