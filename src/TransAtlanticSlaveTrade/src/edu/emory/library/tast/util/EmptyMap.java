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
package edu.emory.library.tast.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class EmptyMap implements Map
{

	public void clear()
	{
	}

	public boolean containsKey(Object key)
	{
		return false;
	}

	public boolean containsValue(Object value)
	{
		return false;
	}

	public Set entrySet()
	{
		return null;
	}

	public Object get(Object key)
	{
		return null;
	}

	public boolean isEmpty()
	{
		return true;
	}

	public Set keySet()
	{
		return new EmptySet();
	}

	public Object put(Object arg0, Object arg1)
	{
		return null;
	}

	public void putAll(Map arg0)
	{
	}

	public Object remove(Object key)
	{
		return null;
	}

	public int size()
	{
		return 0;
	}

	public Collection values()
	{
		return new LinkedList();
	}

}
