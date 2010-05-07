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
package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.util.StringUtils;

public class TextboxDoubleValue extends TextboxValue
{
	
	private boolean parsed = false;
	private boolean valid = false;
	private transient double doubleValue;

	public TextboxDoubleValue(String text)
	{
		super(text);
	}

	public TextboxDoubleValue(double value)
	{
		this(Double.toString(value));
		this.doubleValue = value;
		this.parsed = true;
		this.valid = true;
	}
	
	public TextboxDoubleValue(Double value)
	{
		this(value == null ? "": value.toString());
		if (value != null)
		{
			this.doubleValue = value.intValue();
			this.parsed = true;
			this.valid = true;
		}
		else
		{
			this.parsed = true;
			this.valid = false;
		}
	}

	public void setText(String text)
	{
		super.setText(text);
		parsed = false;
	}
	
	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				doubleValue = Double.parseDouble(getText());
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
	
	public Double getDoubleValue()
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
	
	public Double getFloat()
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
	
	public void setDoubleValue(double value)
	{
		super.setText(String.valueOf(value));
		this.doubleValue = value;
		this.parsed = true;
		this.valid = true;
	}

	public void setFloat(Double value)
	{
		if (value != null)
		{
			this.doubleValue = value.longValue();
			this.parsed = true;
			this.valid = true;
		}
		else
		{
			this.parsed = true;
			this.valid = false;
		}
	}

	public boolean isCorrectValue() {
		if (StringUtils.isNullOrEmpty(this.getText())) {
			return true;
		}
		return isValid();
	}
}