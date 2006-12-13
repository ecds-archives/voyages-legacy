package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.FateSlaves;

public class FateSlavesAttribute extends DictionaryAttribute
{
	
	public FateSlavesAttribute(String name, String objType)
	{
		super(name, objType);
	}
	
	public FateSlavesAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}

	protected Object loadObjectById(Session sess, long id) 
	{
		return FateSlaves.loadById(sess, id);
	}

	public Attribute getAttribute(String name) {
		return FateSlaves.getAttribute(name);
	}

	
}