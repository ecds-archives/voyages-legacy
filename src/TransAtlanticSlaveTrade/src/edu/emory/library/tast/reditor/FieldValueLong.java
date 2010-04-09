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


public class FieldValueLong extends FieldValueText
{
	
	public static final String TYPE = "long";

	private boolean parsed = false;
	private boolean valid = false;
	private transient long longValue;

	public FieldValueLong(String name)
	{
		super(name);
	}

	public FieldValueLong(String name, long longValue)
	{
		super(name);
		setLong(longValue);
	}
	
	public FieldValueLong(String name, Long longValue)
	{
		super(name);
		setLong(longValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				longValue = Long.parseLong(getValue());
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
	
	public Long getLong()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Long(longValue);
		}
	}
	
	public void setLong(long longValue)
	{
		this.longValue = longValue;
		super.setValue(String.valueOf(longValue));
		valid = true;
		parsed = true;
	}

	public void setLong(Long longValue)
	{
		if (longValue != null)
		{
			setLong(longValue.longValue());
		}
		else
		{
			setValue("");
		}
	}

}