package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.EstimatesExportArea;
import edu.emory.library.tast.dm.EstimatesImportArea;

public class EstimatesExportAreaAttribute extends DictionaryAttribute
{
	
	public EstimatesExportAreaAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public EstimatesExportAreaAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return EstimatesImportArea.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return EstimatesImportArea.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return EstimatesImportArea.loadAll(sess);
	}
	
	public NumericAttribute getItAttribute()
	{
		return (NumericAttribute) EstimatesExportArea.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return EstimatesImportArea.class;
	}
	
}
