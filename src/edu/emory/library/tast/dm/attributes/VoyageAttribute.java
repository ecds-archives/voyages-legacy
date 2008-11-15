package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Voyage;

public class VoyageAttribute extends DictionaryAttribute
{
	
	public VoyageAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public VoyageAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Attribute getAttribute(String name)
	{
		return null;
	}

	public List loadAllObjects(Session sess)
	{
		return null;
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) Voyage.getAttribute("iid");
	}

	public Dictionary loadObjectById(Session sess, long id) {
		return null;
	}

	public Class getDictionayClass()
	{
		return Voyage.class;
	}
	
}
