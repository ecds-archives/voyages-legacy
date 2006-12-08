package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;

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
	
	public static Area loadById(long areaId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Area port = loadById(sess, areaId);
		transaction.commit();
		sess.close();
		return port;
	}

	public static Area loadById(Session sess, long areaId)
	{
		List list = sess.createCriteria(Area.class).add(Restrictions.eq("id", new Long(areaId))).list();
		if (list == null || list.size() == 0) return null;
		return (Area) list.get(0);
	}

}
