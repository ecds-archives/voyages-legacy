package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesImportArea extends Location
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesImportArea", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesImportArea"));
		attributes.put("longitude", new StringAttribute("longitude", "Port"));
		attributes.put("latitude", new StringAttribute("latitude", "Port"));
		attributes.put("order", new NumericAttribute("order", "EstimatesImportArea", NumericAttribute.TYPE_INTEGER));
	}
	
	private Set regions;
	
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
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesImportArea.class, sess, "order");
	}
	
	public static Area loadById(Session sess, long portId)
	{
		return (Area) Dictionary.loadById(EstimatesImportArea.class, sess, portId);
	}
	
	public static Area loadById(Session sess, String portId)
	{
		return (Area) Dictionary.loadById(EstimatesImportArea.class, sess, portId);
	}

	public boolean equals(Object that)
	{
		if (that instanceof EstimatesImportArea)
		{
			EstimatesImportArea thatArea = (EstimatesImportArea) that;
			return getId().equals(thatArea.getId());
		}
		else
		{
			return false;
		}
	}

}