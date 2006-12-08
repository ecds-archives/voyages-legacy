package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.dm.VesselRig;

public class VesselRigAttribute extends DictionaryAttribute
{
	
	public static final String ATTR_TYPE_NAME = "VesselRig";
	
	public VesselRigAttribute(String name, String objType)
	{
		super(name, objType);
	}

	public VesselRigAttribute(String name, String objectType, String importName)
	{
		super(name, objectType, importName);
	}
	
	public Object loadObjectById(Session sess, long id) 
	{
		return VesselRig.loadById(sess, id);
	}

}