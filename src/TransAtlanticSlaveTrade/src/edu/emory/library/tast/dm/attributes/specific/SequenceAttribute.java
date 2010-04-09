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

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.util.EmptyMap;

public class SequenceAttribute extends Attribute {

	private Attribute[] attributes;
	
	public SequenceAttribute(Attribute[] attributeSeq) {
		super("SequentialAttribute", null);
		this.attributes = attributeSeq;
	}
	
	public String getTypeDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOuterjoinable() {
		boolean joinable = false;
		for (int i = 0; i < attributes.length; i++) {
			joinable = joinable || attributes[i].isOuterjoinable(); 
		}
		return joinable;
	}
	
	public String getHQLSelectPath(Map bindings) {
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < this.attributes.length; i++) {
			if (i != 0) {
				buffer.append(".");
			}
			buffer.append(attributes[i].getHQLSelectPath(bindings));
		}
		
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}

	public String getHQLOuterJoinPath(Map bindings) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < this.attributes.length; i++) {
			if (attributes[i].getHQLOuterJoinPath(bindings) == null) {
				continue;
			}
			if (i != 0) {
				buffer.append(".");
			}
			buffer.append(attributes[i].getHQLOuterJoinPath(bindings));
		}
		
		return buffer.toString().equals("") ? null : buffer.toString();
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		
		String path = getHQLOuterJoinPath(new EmptyMap());
		String alias = (String) existingJoins.get(path);
		
		if (alias == null)
		{
			
			// the name of the first attribute is the name of the first column
			String prevTableAlias = masterTable;
			
			for (int i = 0; i < attributes.length - 1; i++)
			{
				
				// get the name of the table and the column
				DictionaryAttribute dictAttr = (DictionaryAttribute) attributes[i];
				String table = HibernateConn.getTableName(dictAttr.getDictionayClass());
				String column = HibernateConn.getColumnName("edu.emory.library.tast.dm." + dictAttr.getObjectType(), dictAttr.getName());
				
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
				sqlFrom.append(prevTableAlias).append(".").append(column); //.append(prevTableAlias.startsWith("ports_") || prevTableAlias.startsWith("regions_") ? "_id" : "");
				sqlFrom.append("\n");
				
				// remember for the next round
				prevTableAlias = tableAlias;
				
			}
			
			alias = prevTableAlias;
			existingJoins.put(path, alias);
		
		}
		
		// the alias of the last table
		Attribute lastAttr = attributes[attributes.length - 1];
		return alias + "." + HibernateConn.getColumnName("edu.emory.library.tast.dm." + lastAttr.getObjectType(), lastAttr.getName());
		
	}
	
}
