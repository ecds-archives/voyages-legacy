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
package edu.emory.library.tast.dm.attributes;

import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.spss.LogWriter;
import edu.emory.library.tast.spss.STSchemaVariable;

public class BooleanAttribute extends ImportableAttribute
{
	
	public static final String ATTR_TYPE_NAME = "Boolean";

	public BooleanAttribute(String name, String objectType)
	{
		super(name, objectType);
	}

	public BooleanAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public String getHQLOuterJoinPath(Map bindings)
	{
		return null;
	}

	public String getHQLParamName()
	{
		return this.getName();
	}

	public String getHQLSelectPath(Map bindings)
	{
		if (!bindings.containsKey(this.getObjectType())) {
			return this.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(bindings.get(this.getObjectType()));
		buffer.append(".");
		buffer.append(this.getName());
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings)
	{
		return this.getHQLSelectPath(bindings);
	}

	public String getTypeDisplayName()
	{
		return "Boolean";
	}

	public boolean isOuterjoinable()
	{
		return false;
	}

	public Object importParse(Session sess, String value, LogWriter log, int recordNo)
	{
		return new Boolean(value.equals("TRUE") || value.equals("1"));
	}

	public int getImportType()
	{
		return STSchemaVariable.TYPE_NUMERIC;
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		return masterTable + "." + getName();
	}
	
}
