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
