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
package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;

public class DirectValueAttribute extends Attribute
{

	Object value;

	public DirectValueAttribute(Object value)
	{
		super(null, null);
		this.value = value;
	}

	public DirectValueAttribute(String name, Object value)
	{
		super(name, null);
		this.value = value;
	}

	public String getTypeDisplayName()
	{
		return null;
	}

	public boolean isOuterjoinable()
	{
		return false;
	}

	public String getHQLSelectPath(Map bindings)
	{
		StringBuffer buffer = new StringBuffer();
		if (this.value instanceof String)
		{
			buffer.append("'").append(value).append("'");
		}
		else
		{
			buffer.append(value);
		}
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings)
	{
		return this.getHQLSelectPath(bindings);
	}

	public String getHQLOuterJoinPath(Map bindings)
	{
		return null;
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		StringBuffer buffer = new StringBuffer();
		if (this.value instanceof String)
		{
			buffer.append("'").append(value).append("'");
		}
		else
		{
			buffer.append(value);
		}
		return buffer.toString();
	}
}
