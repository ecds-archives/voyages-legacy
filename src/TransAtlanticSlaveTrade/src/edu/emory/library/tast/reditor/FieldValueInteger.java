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


public class FieldValueInteger extends FieldValueText
{
	
	public static final String TYPE = "integer";

	private boolean parsed = false;
	private boolean valid = false;
	private transient int intValue;

	public FieldValueInteger(String name)
	{
		super(name);
	}

	public FieldValueInteger(String name, int intValue)
	{
		super(name);
		setInteger(intValue);
	}
	
	public FieldValueInteger(String name, Integer intValue)
	{
		super(name);
		setInteger(intValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				intValue = Integer.parseInt(getValue());
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
	
	public Integer getInteger()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Integer(intValue);
		}
	}
	
	public void setInteger(int intValue)
	{
		this.intValue = intValue;
		super.setValue(String.valueOf(intValue));
		valid = true;
		parsed = true;
	}

	public void setInteger(Integer intValue)
	{
		if (intValue != null)
		{
			setInteger(intValue.intValue());
		}
		else
		{
			setValue("");
		}
	}
	

}