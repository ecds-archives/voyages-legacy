package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Dictionary;

public class AreaAttribute extends DictionaryAttribute
{
	
	public AreaAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public AreaAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Area.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Area.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Area.loadAll(sess);
	}

}
