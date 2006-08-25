package edu.emory.library.tast.dm;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.util.HibernateUtil;

public class Port extends Location
{
	
	private Region region;

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}
	
	public static Port loadById(int portId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Port port = loadById(sess, portId);
		transaction.commit();
		sess.close();
		return port;
	}

	public static Port loadById(Session sess, int portId)
	{
		List list = sess.createCriteria(Port.class).add(Restrictions.eq("id", new Integer(portId))).list();
		if (list == null || list.size() == 0) return null;
		return (Port) list.get(0);
	}

	public static List loadAllPorts()
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = loadPorts(sess, null);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List getAllPorts(Session sess)
	{
		return loadPorts(sess, null);
	}

	public static List loadPorts(String substring)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = loadPorts(sess, substring);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List loadPorts(Session sess, String substring)
	{
		Criteria crit = sess.createCriteria(Port.class).
		createAlias("region", "r", Criteria.LEFT_JOIN).
		addOrder(Order.asc("r.name")).
		addOrder(Order.asc("name"));
		if (substring != null) crit.add(Restrictions.ilike("name", substring));
		return crit.list();
	}

}