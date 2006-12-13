package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Area;

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
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return Area.loadById(sess, id);
	}

	public Attribute getAttribute(String name) {
		return Area.getAttribute(name);
	}

}
