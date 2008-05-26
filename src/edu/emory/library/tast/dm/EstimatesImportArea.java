package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesImportArea extends EstimatesArea
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesImportArea", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesImportArea"));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesImportArea", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesImportArea", NumericAttribute.TYPE_FLOAT));
		attributes.put("order", new NumericAttribute("order", "EstimatesImportArea", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "EstimatesImportArea"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesImportArea.class, sess, "order");
	}
	
	public static EstimatesImportArea loadById(Session sess, long portId)
	{
		return (EstimatesImportArea) Dictionary.loadById(EstimatesImportArea.class, sess, portId);
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