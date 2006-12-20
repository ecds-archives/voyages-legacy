package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
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
	
	public Dictionary loadObjectById(Session sess, long id) 
	{
		return VesselRig.loadById(sess, id);
	}

	public Attribute getAttribute(String name)
	{
		return VesselRig.getAttribute(name);
	}

	public List loadAllObjects(Session sess)
	{
		return VesselRig.loadAll(sess);
	}
	
	public NumericAttribute getItAttribute()
	{
		return (NumericAttribute) VesselRig.getAttribute("id");
	}
	
}