package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
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
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Port.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Port.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Port.getAllPorts(sess);
	}
	
}