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


public class FieldValueDouble extends FieldValueText
{
	
	public static final String TYPE = "double";

	private boolean parsed = false;
	private boolean valid = false;
	private transient double doubleValue;

	public FieldValueDouble(String name)
	{
		super(name);
	}

	public FieldValueDouble(String name, double doubleValue)
	{
		super(name);
		setDouble(doubleValue);
	}
	
	public FieldValueDouble(String name, Double doubleValue)
	{
		super(name);
		setDouble(doubleValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				doubleValue = Double.parseDouble(getValue());
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

	public void setDouble(double doubleValue)
	{
		this.doubleValue = doubleValue;
		super.setValue(String.valueOf(doubleValue));
		valid = true;
		parsed = true;
	}

	public void setDouble(Double doubleValue)
	{
		if (doubleValue != null)
		{
			setDouble(doubleValue.doubleValue());
		}
		else
		{
			setValue("");
		}
	}
	
	public Double getDouble()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Double(doubleValue);
		}
	}

	public double getDoubleValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return doubleValue;
		}
	}

}