package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class VesselRig extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "VesselRig", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "VesselRig"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(VesselRig.class, sess);
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(VesselRig.class, sess, orderBy);
	}
	
	public static VesselRig loadById(Session sess, long rigId)
	{
		return (VesselRig) Dictionary.loadById(VesselRig.class, sess, rigId);
	}

	public static VesselRig loadById(Session sess, String rigId)
	{
		return (VesselRig) Dictionary.loadById(VesselRig.class, sess, rigId);
	}

}