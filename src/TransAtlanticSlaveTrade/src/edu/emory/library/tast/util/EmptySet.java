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
