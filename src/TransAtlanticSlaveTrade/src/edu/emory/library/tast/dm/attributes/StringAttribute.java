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

public class StringAttribute extends ImportableAttribute
{
	
	public StringAttribute(String name, String objectType)
	{
		super(name, objectType);
	}
	
	public StringAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}

	public StringAttribute(String name, String objectType, String importName, int maxImportLength)
	{
		super(name, objectType, importName);
		setMaxImportLength(maxImportLength);
	}

	public Object importParse(Session sess, String value, LogWriter log, int recordNo)
	{
		
		if (value == null)
			return null;

		if (isImportLengthLimited() && value.length() > getMaxImportLength())
		{
			log.logWarn(
					"Variable " + getName() + ", " +
					"record " + recordNo + ": " +
					"string '" + value + "' too long. " +
					"Expected maximal length is " + getMaxImportLength() + ". " +
					"Imported as NULL (MISSING).");
			return null;
		}

		return value;

	}

	public String getTypeDisplayName() {
		return "Text";
	}
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_STRING;
	}
	
	public boolean isOuterjoinable() {
		return false;
	}
	
	public String getHQLSelectPath(Map bindings) {
		if (!bindings.containsKey(this.getObjectType())) {
			return this.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(bindings.get(this.getObjectType()));
		buffer.append(".");
		buffer.append(this.getName());
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}
	
	public String getHQLParamName() {
		return this.getName();
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		return null;
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		return masterTable + "." + getName();
	}

}
