package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
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

	public Dictionary loadObjectById(Session sess, long id) 
	{
		return FateSlaves.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return FateSlaves.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return FateSlaves.loadAll(sess);
	}
	
	public NumericAttribute getItAttribute()
	{
		return (NumericAttribute) FateSlaves.getAttribute("id");
	}
	
}