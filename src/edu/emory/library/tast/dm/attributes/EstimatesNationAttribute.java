package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.EstimatesNation;

public class EstimatesNationAttribute extends DictionaryAttribute
{
	
	public EstimatesNationAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public EstimatesNationAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return EstimatesNation.loadById(sess, id);
	}
	
}
