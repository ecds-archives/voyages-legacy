package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Resistance;

public class ResistanceAttribute extends DictionaryAttribute
{
	
	public ResistanceAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public ResistanceAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return Resistance.loadById(sess, id);
	}

	public Attribute getAttribute(String name) {
		return Resistance.getAttribute(name);
	}

	
}