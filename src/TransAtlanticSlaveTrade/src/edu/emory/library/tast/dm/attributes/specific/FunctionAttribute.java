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

public class FunctionAttribute extends Attribute {

	private Attribute[] attributes; 
	private String functionName;
	
	public FunctionAttribute(String functionName, Attribute[] attributes)
	{
		super("FunctionAttribute", null);
		this.attributes = attributes;
		this.functionName = functionName;
	}
	
	public FunctionAttribute(String functionName, Attribute attribute)
	{
		super("FunctionAttribute", null);
		this.attributes = new Attribute[] {attribute};
		this.functionName = functionName;
	}

	public String getTypeDisplayName() {
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
		buffer.append(this.functionName).append("(");
		for (int i = 0; i < this.attributes.length; i++) {
			if (i != 0) {
				buffer.append(", ");
			}
			buffer.append(attributes[i].getHQLSelectPath(bindings));
		}
		buffer.append(")");
		
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
				buffer.append(", ");
			}
			buffer.append(attributes[i].getHQLOuterJoinPath(bindings));
		}
		
		return buffer.toString().equals("") ? null : buffer.toString();
	}

	public String getFunctionName()
	{
		return functionName;
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.functionName).append("(");
		for (int i = 0; i < this.attributes.length; i++)
		{
			if (i > 0) buffer.append(", ");
			buffer.append(attributes[i].getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
		}
		buffer.append(")");
		return buffer.toString();	
	}

}
