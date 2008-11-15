package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.FateVessel;

public class FateVesselAttribute extends DictionaryAttribute
{
	
	public FateVesselAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public FateVesselAttribute(String name, String objType, String importName)
	{
		super(name, objType, importName);
	}
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return FateVessel.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return FateVessel.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return FateVessel.loadAll(sess);
	}
	
	public NumericAttribute getIdAttribute()
	{
		return (NumericAttribute) FateVessel.getAttribute("id");
	}

	public Class getDictionayClass()
	{
		return FateVessel.class;
	}
	
}