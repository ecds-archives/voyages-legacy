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

public class FieldValueCheckbox extends FieldValue
{

	public static final String TYPE = "checkbox";
	
	private boolean checked;

	public FieldValueCheckbox(String name)
	{
		super(name);
	}

	public FieldValueCheckbox(String name, boolean checked)
	{
		super(name);
		this.checked = checked;
	}

	public FieldValueCheckbox(String name, Boolean checked)
	{
		super(name);
		this.checked = checked != null && checked.booleanValue();
	}

	public static String getHtmlFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name;
	}

	public void decode(UIComponent component, FacesContext context)
	{
		String htmlFieldName = getHtmlFieldName(component, context, getName());
		checked = context.getExternalContext().getRequestParameterMap().containsKey(htmlFieldName);
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public Boolean getBoolean()
	{
		return new Boolean(checked);
	}

}
