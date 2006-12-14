package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesExportRegion extends DictionaryOrdered
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesExportRegion", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesExportRegion"));
		attributes.put("order", new NumericAttribute("name", "EstimatesExportRegion", NumericAttribute.TYPE_INTEGER));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesExportRegion.class, sess, "order");
	}
	
	public static EstimatesExportRegion loadById(Session sess, long portId)
	{
		return (EstimatesExportRegion) Dictionary.loadById(EstimatesExportRegion.class, sess, portId);
	}

}