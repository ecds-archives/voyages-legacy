package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.FateOwner;

public class FateOwnerAttribute extends DictionaryAttribute
{
	
	public FateOwnerAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateOwnerAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return FateOwner.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return FateOwner.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return FateOwner.loadAll(sess);
	}
	
}