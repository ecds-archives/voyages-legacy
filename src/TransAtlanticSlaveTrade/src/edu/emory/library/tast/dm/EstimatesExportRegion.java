package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesExportAreaAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EstimatesExportRegion extends EstimatesRegion
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesExportRegion", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesExportRegion"));
		attributes.put("area", new EstimatesExportAreaAttribute("area", "EstimatesExportRegion"));
		attributes.put("order", new NumericAttribute("order", "EstimatesExportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("longitude", new NumericAttribute("longitude", "EstimatesExportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "EstimatesExportRegion", NumericAttribute.TYPE_FLOAT));
		attributes.put("showAtZoom", new NumericAttribute("showAtZoom", "EstimatesExportRegion", NumericAttribute.TYPE_INTEGER));
		attributes.put("showOnMap", new BooleanAttribute("showOnMap", "EstimatesExportRegion"));
	}
	
	private EstimatesExportArea area;
	
	public EstimatesExportArea getArea()
	{
		return area;
	}

	public void setArea(EstimatesExportArea area)
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
		return Dictionary.loadAll(EstimatesExportRegion.class, sess, "order");
	}
	
	public static EstimatesExportRegion loadById(Session sess, long portId)
	{
		return (EstimatesExportRegion) Dictionary.loadById(EstimatesExportRegion.class, sess, portId);
	}

	public static EstimatesExportRegion loadById(Session sess, String portId)
	{
		return (EstimatesExportRegion) Dictionary.loadById(EstimatesExportRegion.class, sess, portId);
	}

}