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
import java.util.Iterator;
import java.util.Set;

public class EmptySet implements Set
{

	public boolean add(Object arg0)
	{
		return false;
	}

	public boolean addAll(Collection arg0)
	{
		return false;
	}

	public void clear()
	{
	}

	public boolean contains(Object o)
	{
		return false;
	}

	public boolean containsAll(Collection arg0)
	{
		return false;
	}

	public boolean isEmpty()
	{
		return true;
	}

	public Iterator iterator()
	{
		return null;
	}

	public boolean remove(Object o)
	{
		return false;
	}

	public boolean removeAll(Collection arg0)
	{
		return false;
	}

	public boolean retainAll(Collection arg0)
	{
		return false;
	}

	public int size()
	{
		return 0;
	}

	public Object[] toArray()
	{
		return new Object[] {};
	}

	public Object[] toArray(Object[] arg0)
	{
		return new Object[] {};
	}

}
