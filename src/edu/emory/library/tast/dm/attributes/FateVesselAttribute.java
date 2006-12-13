package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

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
	
	protected Object loadObjectById(Session sess, long id) 
	{
		return FateVessel.loadById(sess, id);
	}

	public Attribute getAttribute(String name) {
		return FateVessel.getAttribute(name);
	}

	
}