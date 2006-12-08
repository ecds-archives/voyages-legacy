package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Insurrections;

public class InsurrectionsAttribute extends DictionaryAttribute
{
	
	public InsurrectionsAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public InsurrectionsAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return Insurrections.loadById(sess, id);
	}

}