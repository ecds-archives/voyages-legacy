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
import edu.emory.library.tast.dm.Fate;

public class FateAttribute extends DictionaryAttribute
{
	
	public FateAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}

	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Fate.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Fate.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Fate.loadAll(sess);
	}

	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) Fate.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return Fate.class;
	}

}
