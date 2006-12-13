package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Region;

public class RegionAttribute extends DictionaryAttribute
{
	
	public RegionAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public RegionAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Region.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Region.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Region.loadAll(sess);
	}
	
}
