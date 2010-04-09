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
package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

/**
 * Abstract class that provides basic methods for storing properties in object specific classes.
 * @author Pawel Jurczyk
 *
 */
public abstract class AbstractDescriptiveObject
{
	
	/**
	 * Current attribute values.
	 */
	protected Map values = new HashMap(500);
	
	/**
	 * Gets value of given attribute
	 * @param p_attrName attribut name
	 * @return Object representing current attribute value
	 */
	public Object getAttrValue(String p_attrName)
	{
		return values.get(p_attrName);
	}

	/**
	 * Sets value of given attribute.
	 * @param p_attrName	attribut name
	 * @param p_attrValue	new attribute value
	 */
	public void setAttrValue(String p_attrName, Object p_attrValue)
	{
		values.put(p_attrName, p_attrValue);
	}

	/**
	 * Gets values of all attributes.
	 * @return Map of values
	 */
	public Map getAllAttrValues()
	{
		return values;
	}
	
	public abstract void saveOrUpdate();
	public abstract void saveOrUpdate(Session sess);

}
