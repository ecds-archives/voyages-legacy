package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;

public class Region extends Location
{
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new StringAttribute("id", "Region"));
		attributes.put("name", new StringAttribute("name", "Region"));
		attributes.put("x", new NumericAttribute("x", "Region"));
		attributes.put("y", new NumericAttribute("y", "Region"));
		attributes.put("ports", new NumericAttribute("ports", "Region"));
	}
	
	private Set ports;

	public Set getPorts()
	{
		return ports;
	}

	public void setPorts(Set ports)
	{
		this.ports = ports;
	}
	
	public static Region[] getRegionsArray()
	{
		List list = loadAllRegions();
		Region[] regions = new Region[list.size()];
		list.toArray(regions);
		return regions;
	}
	
	public static List loadAllRegions()
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = loadRegions(sess, null);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List getAllRegions(Session sess)
	{
		return loadRegions(sess, null);
	}

	public static List loadRegions(String substring)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = loadRegions(sess, substring);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List loadRegions(Session sess, String substring)
	{
		Criteria crit = sess.createCriteria(Region.class).addOrder(Order.asc("name"));
		if (substring != null) crit.add(Restrictions.ilike("name", substring));
		return crit.list();
	}
	
	public static Region loadById(int regionId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Region region = loadById(sess, regionId);
		transaction.commit();
		sess.close();
		return region;
	}

	public static Region loadById(Session sess, int regionId)
	{
		List list = sess.createCriteria(Region.class).add(Restrictions.eq("remoteId", new Integer(regionId))).list();
		if (list == null || list.size() == 0) return null;
		return (Region) list.get(0);
	}
	
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

	//	public static void main(String[] args)
//	{
//		
//		Region[] regions = getRegions();
//		for (int i = 0; i < regions.length; i++)
//		{
//			Region region = regions[i];
//			System.out.println(region.getId() + " : " + region.getName());
//			for (Iterator iterPort = region.getPorts().iterator(); iterPort.hasNext();)
//			{
//				Port port = (Port) iterPort.next();
//				System.out.println(port.getId() + " : " + port.getName());
//			}
//			System.out.println("============================================");
//		}
//		
//	}

}