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

public class TextboxIntegerValue extends TextboxValue
{
	
	private boolean parsed = false;
	private boolean valid = false;
	private transient int intValue;

	public TextboxIntegerValue(String text)
	{
		super(text);
	}

	public TextboxIntegerValue(int value)
	{
		this(Integer.toString(value));
		this.intValue = value;
		this.parsed = true;
		this.valid = true;
	}
	
	public TextboxIntegerValue(Integer value)
	{
		this(value == null ? "": value.toString());
		if (value != null)
		{
			this.intValue = value.intValue();
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
				intValue = Integer.parseInt(getText());
				valid = true;
			}
			catch(NumberFormatException nfe)
			{
				valid = false;
			} catch (NullPointerException npe) {
				valid = false;
			}
			parsed = true;
		}
		return valid;
	}
	
	public int getIntValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return intValue;
		}
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
	
	public void setIntValue(int value)
	{
		super.setText(String.valueOf(value));
		this.intValue = value;
		this.parsed = true;
		this.valid = true;
	}

	public void setInteger(Integer value)
	{
		if (value != null)
		{
			this.intValue = value.intValue();
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