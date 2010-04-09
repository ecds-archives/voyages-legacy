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

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.spss.LogWriter;
import edu.emory.library.tast.spss.STSchemaVariable;

public abstract class DictionaryAttribute extends ImportableAttribute
{
	public DictionaryAttribute(String name, String objType)
	{
		this(name, objType, null);
	}
	
	public DictionaryAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public abstract NumericAttribute getIdAttribute(); 
	
	public int getImportType()
	{
		return STSchemaVariable.TYPE_NUMERIC;
	}
	
	public Object importParse(Session sess, String value, LogWriter log, int recordNo) 
	{
		
		if (value == null || value.length() == 0)
			return null;
		
		long id = 0;
		
		try
		{
			id = Long.parseLong(value);
		}
		catch (NumberFormatException nfe)
		{
			log.logWarn(
					"Variable " + getName() + ", " +
					"record " + recordNo + ": " +
					"values '" + value + "' is expected to be an integer. " +
					"Imported as NULL (MISSING).");
			return null;
		}
		
		Object obj = loadObjectById(sess, id);
		if (obj == null)
		{
			log.logWarn(
					"Variable " + getName() + ", " +
					"record " + recordNo + ": " +
					"label for code '" + value + "' is not defined. " +
					"Imported as NULL (MISSING).");
			return null;
		}

		return obj;

	}

	public abstract Dictionary loadObjectById(Session sess, long id);
	public abstract List loadAllObjects(Session sess);
	public abstract Class getDictionayClass();

	public boolean isOuterjoinable() {
		return true;
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
		if (!bindings.containsKey(this.getObjectType())) {
			return this.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(bindings.get(this.getObjectType()));
		buffer.append(".");
		buffer.append(this.getName());
		return buffer.toString();
	}

	public abstract Attribute getAttribute(String name);
	
	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{

		// get the name of the table
		String table = HibernateConn.getTableName(this.getDictionayClass().getCanonicalName());
		
		// get a new index for this table
		int thisIdx = 0;
		Integer lastIdx = (Integer)tablesIndexes.get(table);
		if (lastIdx != null) thisIdx = lastIdx.intValue() + 1; 
		tablesIndexes.put(table, new Integer(thisIdx));
		
		// create an alias for it using the index
		String tableAlias = table + "_" + thisIdx;

		// INNER JOIN table tableAlias ON tableAlias.id = prevTableAlias.prevColumn
		sqlFrom.append("    ");
		sqlFrom.append("LEFT JOIN ");
		sqlFrom.append(table).append(" ").append(tableAlias);
		sqlFrom.append(" ON ");
		sqlFrom.append(tableAlias).append(".").append("id");
		sqlFrom.append(" = ");					
		sqlFrom.append(masterTable).append(".").append(getName());
		sqlFrom.append("\n");		
		
		// name of the column from the 'dictionary' is always 'name'
		return tableAlias + ".name";

	}

}
