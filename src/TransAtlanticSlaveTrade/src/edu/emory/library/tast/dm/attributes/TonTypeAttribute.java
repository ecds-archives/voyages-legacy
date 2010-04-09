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

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.TonType;

public class TonTypeAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "tonType";
	
	public TonTypeAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public TonTypeAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return TonType.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return TonType.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return TonType.loadAll(sess);
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) TonType.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return TonType.class;
	}
	
}