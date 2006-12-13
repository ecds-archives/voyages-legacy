package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

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

	protected Object loadObjectById(Session sess, long id) 
	{
		return Fate.loadById(sess, id);
	}

	public Attribute getAttribute(String name) {
		return Fate.getAttribute(name);
	}

}
