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
package edu.emory.library.tast.util;

public class StringAggregator
{
	
	private String separator = null;
	private StringBuffer buff = new StringBuffer();
	
	public StringAggregator()
	{
	}
	
	public StringAggregator(String separator)
	{
		this.separator = separator;
	}
	
	public void append(String str)
	{
		if (separator != null && buff.length() > 0) buff.append(separator);
		buff.append(str);
	}
	
	public void appendConditionaly(boolean condition, String str)
	{
		if (condition) append(str);
	}

	public void appendIfNotNull(String str)
	{
		if (str != null) append(str);
	}

	public void appendIfNotNullOrEmpty(String str)
	{
		if (!StringUtils.isNullOrEmpty(str)) append(str);
	}

	public void reset()
	{
		buff.setLength(0);
	}
	
	public boolean isEmpty()
	{
		return buff.length() == 0;
	}

	public String toString()
	{
		return buff.toString();
	}
	
}