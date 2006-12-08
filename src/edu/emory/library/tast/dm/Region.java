package edu.emory.library.tast.dm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.AreaAttribute;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;

public class Region extends Location
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Region", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Region"));
		attributes.put("x", new NumericAttribute("x", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("y", new NumericAttribute("y", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("latitude", new NumericAttribute("latitude", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("longitude", new NumericAttribute("longitude", "Region", NumericAttribute.TYPE_FLOAT));
		attributes.put("ports", new NumericAttribute("ports", "Region", NumericAttribute.TYPE_LONG));
		attributes.put("area", new AreaAttribute("area", "Region"));
		attributes.put("order", new NumericAttribute("order", "Region", NumericAttribute.TYPE_INTEGER));
	}
	
	private Set ports;
	private Area area;
	private int order;
	
	public Area getArea()
	{
		return area;
	}

	public void setArea(Area area)
	{
		this.area = area;
	}

	public Set getPorts()
	{
		return ports;
	}

	public void setPorts(Set ports)
	{
		this.ports = ports;
	}
	
	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
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
	
	public static List loadAllRegions(Session sess)
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
	
	public static Region loadById(long regionId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Region region = loadById(sess, regionId);
		transaction.commit();
		sess.close();
		return region;
	}

	public static Region loadById(Session sess, long regionId)
	{
		List list = sess.createCriteria(Region.class).add(Restrictions.eq("id", new Long(regionId))).setCacheable(true).list();
		if (list == null || list.size() == 0) return null;
		return (Region) list.get(0);
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

	public static String[] regionNamesToArray(List regions)
	{
		String[] names = new String[regions.size()];
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			names[i++] = region.getName();
		}
		
		return names;
	}
	
	public static Map createIdIndexMap(List regions)
	{
		Map map = new HashMap();
		
		int i = 0;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			map.put(region.getId(), new Integer(i++));
		}
		
		return map;
	}

	public static List sortRegionsByArea(List regions)
	{
		Region regionsArray[] = new Region[regions.size()];
		regions.toArray(regionsArray);
		Arrays.sort(regionsArray, new Comparator() {
			public int compare(Object obj0, Object obj1)
			{
				Area a0 = ((Region) obj0).getArea();
				Area a1 = ((Region) obj1).getArea();
				if (a0 == null) return -1;
				if (a1 == null) return 1;
				return a0.getName().compareTo(a1.getName());
			}});
		List newList = new ArrayList();
		for (int i = 0; i < regionsArray.length; i++) newList.add(regionsArray[i]);
		return newList;
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