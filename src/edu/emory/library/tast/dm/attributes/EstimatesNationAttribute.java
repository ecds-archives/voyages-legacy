package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
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
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return EstimatesNation.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return EstimatesNation.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return EstimatesNation.loadAll(sess);
	}
	
}
