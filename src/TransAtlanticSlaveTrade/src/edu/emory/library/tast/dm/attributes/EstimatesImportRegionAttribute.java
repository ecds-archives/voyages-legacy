package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.EstimatesImportRegion;

public class EstimatesImportRegionAttribute extends DictionaryAttribute
{
	
	public EstimatesImportRegionAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public EstimatesImportRegionAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return EstimatesImportRegion.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return EstimatesImportRegion.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return EstimatesImportRegion.loadAll(sess);
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) EstimatesImportRegion.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return EstimatesImportRegion.class;
	}
	
}
