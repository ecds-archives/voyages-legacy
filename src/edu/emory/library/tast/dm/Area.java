package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Area extends Location
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Area", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Area"));
		attributes.put("latitude", new NumericAttribute("latitude", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("longitude", new NumericAttribute("longitude", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("regions", new NumericAttribute("regions", "Area", NumericAttribute.TYPE_LONG));
		attributes.put("order", new NumericAttribute("order", "Area", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "Area", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "Area"));
	}
	
	private Set regions;
	private boolean showOnMap;

	public boolean isShowOnMap()
	{
		return showOnMap;
	}

	public void setShowOnMap(boolean showOnMap)
	{
		this.showOnMap = showOnMap;
	}

	public Set getRegions()
	{
		return regions;
	}

	public void setRegions(Set regions)
	{
		this.regions = regions;
	}

	public static Attribute getAttribute(String name)
	{
		if (!attributes.containsKey(name)) {
			throw new RuntimeException("area attribute not defined: " + name);
		}
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Area.class, sess, "order");
	}
	
	public static List loadAll(Session sess, String orderBy)
	{
		return Dictionary.loadAll(Area.class, sess, orderBy);
	}

	public static Area loadById(Session sess, long portId)
	{
		return (Area) Dictionary.loadById(Area.class, sess, portId);
	}

	public static Area loadById(Session sess, String portId)
	{
		return (Area) Dictionary.loadById(Area.class, sess, portId);
	}

}
