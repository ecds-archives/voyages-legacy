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

public abstract class FieldValue
{
	
	private String name;
	
	public abstract void decode(UIComponent component, FacesContext context);

	public FieldValue(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public static FieldValue createFieldValue(FieldSchema fieldSchema)
	{
		return FieldValue.createFieldValue(fieldSchema.getName(), fieldSchema.getType());
	}

	public static FieldValue createFieldValue(FieldSchemaState fieldState)
	{
		return FieldValue.createFieldValue(fieldState.getName(), fieldState.getType());
	}

	public static FieldValue createFieldValue(String name, String type)
	{
		if (type == null)
		{
			throw new RuntimeException("field type null");
		}
		else if (FieldValueText.TYPE.equals(type))
		{
			return new FieldValueText(name);
		}
		else if (FieldValueCheckbox.TYPE.equals(type))
		{
			return new FieldValueCheckbox(name);
		}
		else if (FieldValueDropdowns.TYPE.equals(type))
		{
			return new FieldValueDropdowns(name);
		}
		else if (FieldValueDate.TYPE.equals(type))
		{
			return new FieldValueDate(name);
		}
		else if (FieldValueInteger.TYPE.equals(type))
		{
			return new FieldValueInteger(name);
		}
		else if (FieldValueLong.TYPE.equals(type))
		{
			return new FieldValueLong(name);
		}
		else if (FieldValueFloat.TYPE.equals(type))
		{
			return new FieldValueFloat(name);
		}
		else if (FieldValueDouble.TYPE.equals(type))
		{
			return new FieldValueDouble(name);
		}
		else
		{
			throw new RuntimeException("unsupported field type '" + type + "' for '" + name + "'");
		}
	}
	
}