package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Port extends LocationWithImages
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Port", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Port"));
		attributes.put("longitude", new StringAttribute("longitude", "Port"));
		attributes.put("latitude", new StringAttribute("latitude", "Port"));
		attributes.put("region", new RegionAttribute("region", "Port"));
		attributes.put("order", new NumericAttribute("order", "Port", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "Port", NumericAttribute.TYPE_INTEGER));
	}

	private Region region;

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Port.class, sess, "order");
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Port.class, sess, orderBy);
	}

	public static Port loadById(Session sess, long portId)
	{
		return (Port) Dictionary.loadById(Port.class, sess, portId);
	}

	public static Port loadById(Session sess, String portId)
	{
		return (Port) Dictionary.loadById(Port.class, sess, portId);
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}
