package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesExportArea extends EstimatesArea
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesExportArea", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesExportArea"));
		attributes.put("order", new NumericAttribute("order", "EstimatesExportArea", NumericAttribute.TYPE_INTEGER));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesExportArea", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesExportArea", NumericAttribute.TYPE_FLOAT));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "EstimatesExportArea"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesExportArea.class, sess, "order");
	}
	
	public static EstimatesImportArea loadById(Session sess, long portId)
	{
		return (EstimatesImportArea) Dictionary.loadById(EstimatesExportArea.class, sess, portId);
	}
	
	public static Area loadById(Session sess, String portId)
	{
		return (Area) Dictionary.loadById(EstimatesExportArea.class, sess, portId);
	}

	public boolean equals(Object that)
	{
		if (that instanceof EstimatesExportArea)
		{
			EstimatesExportArea thatArea = (EstimatesExportArea) that;
			return getId().equals(thatArea.getId());
		}
		else
		{
			return false;
		}
	}

}