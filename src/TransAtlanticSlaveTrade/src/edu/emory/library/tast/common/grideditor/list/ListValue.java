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
package edu.emory.library.tast.common.grideditor.list;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class ListValue extends Value
{
	
	private String values[];
	
	public ListValue()
	{
	}

	public ListValue(String[] values)
	{
		this.values = values;
	}

	public String[] getValues()
	{
		return values;
	}

	public void setValues(String[] values)
	{
		this.values = values;
	}
	
	public String toString()
	{
		return StringUtils.join("/", values);
	}

	public boolean equals(Object obj)
	{

		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof ListValue))
			return false;

		ListValue that = (ListValue) obj;

		return StringUtils.compareStringArrays(this.values, that.values);
		
	}
	
	public boolean isCorrectValue() {
		return true;
	}

	public boolean isEmpty() {
		return values == null || values.length == 0;
	}
	
}