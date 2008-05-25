package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.EstimatesImportAreaAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesImportRegion extends EstimatesRegion
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesImportRegion", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesImportRegion"));
		attributes.put("area", new EstimatesImportAreaAttribute("area", "EstimatesImportRegion"));
		attributes.put("order", new NumericAttribute("order", "EstimatesImportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "EstimatesImportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesImportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesImportRegion", NumericAttribute.TYPE_FLOAT));
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
	
	public EstimatesArea getAbstractArea()
	{
		return area;
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(EstimatesImportRegion.class, sess, "order");
	}
	
	public static EstimatesImportRegion loadById(Session sess, long portId)
	{
		return (EstimatesImportRegion) Dictionary.loadById(EstimatesImportRegion.class, sess, portId);
	}

	public static EstimatesImportRegion loadById(Session sess, String portId)
	{
		return (EstimatesImportRegion) Dictionary.loadById(EstimatesImportRegion.class, sess, portId);
	}

}