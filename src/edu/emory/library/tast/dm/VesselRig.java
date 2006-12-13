package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;

public class VesselRig extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "VesselRig", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "VesselRig"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static List loadAll(Session sess)
	{
		return sess.createCriteria(VesselRig.class).
		addOrder(Order.asc("name")).
		list();
	}
	
	public static VesselRig loadById(long rigId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		VesselRig rig = loadById(sess, rigId);
		transaction.commit();
		sess.close();
		return rig;
	}

	public static VesselRig loadById(Session sess, long rigId)
	{
		List list = sess.createCriteria(VesselRig.class).add(Restrictions.eq("id", new Long(rigId))).setCacheable(true).list();
		if (list == null || list.size() == 0) return null;
		return (VesselRig) list.get(0);
	}

}