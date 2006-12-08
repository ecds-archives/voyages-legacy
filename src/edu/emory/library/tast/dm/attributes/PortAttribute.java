package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Port;

public class PortAttribute extends DictionaryAttribute
{
	
	public PortAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public PortAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return Port.loadById(sess, id);
	}

}