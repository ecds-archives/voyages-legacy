package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Area extends Dictionary
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Area", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Area"));
		attributes.put("x", new NumericAttribute("x", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("y", new NumericAttribute("y", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("longitude", new NumericAttribute("longitude", "Area", NumericAttribute.TYPE_FLOAT));
		attributes.put("regions", new NumericAttribute("regions", "Area", NumericAttribute.TYPE_LONG));
	}
	
	private int order;
	private boolean america;
	
	public boolean isAmerica()
	{
		return america;
	}

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public void setAmerica(boolean america)
	{
		this.america = america;
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Area.class, sess);
	}
	
	public static Area loadById(Session sess, long portId)
	{
		return (Area) Dictionary.loadById(Area.class, sess, portId);
	}

}
