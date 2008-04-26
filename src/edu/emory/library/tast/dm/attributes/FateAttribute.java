package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
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

	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Fate.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Fate.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Fate.loadAll(sess);
	}

	public NumericAttribute getItAttribute()
	{
		return (NumericAttribute) Fate.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return Fate.class;
	}

}
