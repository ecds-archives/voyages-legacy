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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FieldValueDropdowns extends FieldValue
{
	
	public static final String TYPE = "dropdowns";
	
	private String values[];
	
	public FieldValueDropdowns(String name)
	{
		super(name);
	}
	
	public FieldValueDropdowns(String name, String values[])
	{
		super(name);
		this.values = values;
	}

	public static String getHtmlCountHiddenFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name + "_count";
	}

	public static String getHtmlSelectName(UIComponent component, FacesContext context, String name, int listIndex)
	{
		return component.getClientId(context) + "_" + name + "_" + listIndex;
	}
	
	public String[] getValues()
	{
		return values;
	}

	public void setValues(String[] values)
	{
		this.values = values;
	}
	
	public String getValue(int index)
	{
		if (values == null || index < 0 || values.length <= index)
		{
			return null;
		}
		else
		{
			return values[index];
		}
	}

	public void decode(UIComponent component, FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();

		String countFieldName = FieldValueDropdowns.getHtmlCountHiddenFieldName(component, context, getName());
		int count = Integer.parseInt((String) params.get(countFieldName));
		
		values = new String[count];
		
		for (int i = 0; i < count; i++)
		{
			String htmlFieldName = FieldValueDropdowns.getHtmlSelectName(component, context, getName(), i);
			values[i] = (String) params.get(htmlFieldName);
		}
		
	}

}