package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
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
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return Nation.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return Nation.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return Nation.loadAll(sess);
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) Nation.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return Nation.class;
	}

}
