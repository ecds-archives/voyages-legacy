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


public class FieldValueFloat extends FieldValueText
{
	
	public static final String TYPE = "float";

	private boolean parsed = false;
	private boolean valid = false;
	private transient float floatValue;

	public FieldValueFloat(String name)
	{
		super(name);
	}

	public FieldValueFloat(String name, float doubleValue)
	{
		super(name);
		setFloat(doubleValue);
	}
	
	public FieldValueFloat(String name, Float doubleValue)
	{
		super(name);
		setFloat(doubleValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				floatValue = Float.parseFloat(getValue());
				valid = true;
			}
			catch(NumberFormatException nfe)
			{
				valid = false;
			}
			parsed = true;
		}
		return valid;

	}
	
	public void setValue(String value)
	{
		parsed = false;
		super.setValue(value);
	}

	public void setFloat(float floatValue)
	{
		this.floatValue = floatValue;
		super.setValue(String.valueOf(floatValue));
		valid = true;
		parsed = true;
	}

	public void setFloat(Float floatValue)
	{
		if (floatValue != null)
		{
			setFloat(floatValue.floatValue());
		}
		else
		{
			setValue("");
		}
	}
	
	public Float getFloat()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Float(floatValue);
		}
	}

	public float getFloatValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return floatValue;
		}
	}

}