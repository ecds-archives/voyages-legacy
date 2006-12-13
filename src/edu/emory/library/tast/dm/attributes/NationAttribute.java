package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Nation;

public class NationAttribute extends DictionaryAttribute
{
	
	public NationAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public NationAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return Nation.loadById(sess, id);
	}

	public Attribute getAttribute(String name) {
		return Nation.getAttribute(name);
	}

	
}
