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

import org.w3c.dom.Node;

public abstract class Attribute  {

	private static final long serialVersionUID = -8780232223504322861L;

	private String objectType;
	
	private String name;

	private String userLabel;

	private String description;

	public Attribute(String name, String objectType)
	{
		this.name = name;
		this.objectType = objectType;
	}
	
	protected String parseAttribute(Node xmlNode, String attributeName) {
		Node namedItem = xmlNode.getAttributes().getNamedItem(attributeName);
		if (namedItem != null) {
			String attribute = namedItem.getNodeValue();
			if (attribute != null && !"".equals(attribute)) {
				return attribute;
			}
		}
		return null;
	}

	public String encodeToString() {
		return "Attribute_" + this.getName();
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	
	public String getObjectType()
	{
		return objectType;
	}	

	public String getName() {
		return name;
	}

	public void setName(String attrName) {
		this.name = attrName;
	}

	public String getUserLabelOrName()
	{
		if (userLabel != null && userLabel.length() > 0) return userLabel;
		if (name != null && name.length() > 0) return name;
		return "";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		if (this.userLabel != null && !this.userLabel.equals("")) {
			return this.userLabel;
		} else {
			return this.name;
		}
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Attribute))
			return false;
		Attribute theOther = (Attribute) obj;
		return (name == null && theOther.getName() == null)
				|| (name != null && name.equals(theOther.getName()));
	}

	public int hashCode() {
		if (name == null)
			return super.hashCode();
		return name.hashCode();
	}
	
	public abstract String getHQLWherePath(Map bindings);
	
	public abstract String getHQLSelectPath(Map bindings);

	public abstract boolean isOuterjoinable();
	
	public abstract String getHQLOuterJoinPath(Map bindings);
	
	public abstract String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom);
	
}