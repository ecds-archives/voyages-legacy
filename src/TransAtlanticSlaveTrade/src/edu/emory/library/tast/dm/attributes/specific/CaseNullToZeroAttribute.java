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

public class CaseNullToZeroAttribute extends Attribute {
	
	private Attribute attribute;
	
	/**
	 * case when sum(v.slaximp) is null then 0 else sum(v.slaximp) end
	 */
	
	public CaseNullToZeroAttribute(Attribute attribute) {
		super(attribute.getName(), null);
		this.attribute = attribute;
	}
	
	public String getTypeDisplayName() {
		return null;
	}

	public boolean isOuterjoinable() {
		return attribute.isOuterjoinable();
	}
	
	public String getHQLSelectPath(Map bindings) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("case when ");
		buffer.append(attribute.getHQLSelectPath(bindings));
		buffer.append(" is null then 0 else ");
		buffer.append(attribute.getHQLSelectPath(bindings));
		buffer.append(" end");
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}
	
	public String getHQLOuterJoinPath(Map bindings) {
		return attribute.getHQLOuterJoinPath(bindings);
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("CASE WHEN ");
		buffer.append(attribute.getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
		buffer.append(" IS NULL THEN 0 ELSE ");
		buffer.append(attribute.getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
		buffer.append(" END");
		return buffer.toString();
	}

}
