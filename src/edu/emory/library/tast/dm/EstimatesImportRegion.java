package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesImportRegion extends DictionaryOrdered
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesImportRegion", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesImportRegion"));
		attributes.put("order", new NumericAttribute("name", "EstimatesImportRegion", NumericAttribute.TYPE_INTEGER));
	}
	
	private EstimatesImportArea area;
	
	public EstimatesImportArea getArea()
	{
		return area;
	}

	public void setArea(EstimatesImportArea area)
	{
		this.area = area;
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesImportRegion.class, sess, "order");
	}
	
	public static Area loadById(Session sess, long portId)
	{
		return (Area) Dictionary.loadById(EstimatesImportRegion.class, sess, portId);
	}

}