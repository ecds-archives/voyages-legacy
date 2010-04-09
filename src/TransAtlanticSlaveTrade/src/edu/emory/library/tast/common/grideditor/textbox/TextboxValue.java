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

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class TextboxValue extends Value
{
	
	private String text;

	public TextboxValue(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public boolean isEmptyOrNull()
	{
		return StringUtils.isNullOrEmpty(text);
	}
	
	public String toString()
	{
		return text;
	}
	
	public boolean equals(Object obj)
	{
		
		if (obj == this)
			return true;
		
		if (obj == null)
			return false;
		
		if (!(obj instanceof TextboxValue))
			return false;
		
		TextboxValue that = (TextboxValue) obj;
		
		return StringUtils.compareStrings(this.text, that.text, true);
		
	}

	public boolean isCorrectValue() {
		return true;
	}

	public boolean isEmpty() {
		return text == null || text.length() == 0;
	}
}