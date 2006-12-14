package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.EstimatesExportRegion;

public class EstimatesExportRegionAttribute extends DictionaryAttribute
{
	
	public EstimatesExportRegionAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public EstimatesExportRegionAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return EstimatesExportRegion.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return EstimatesExportRegion.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return EstimatesExportRegion.loadAll(sess);
	}
	
}
