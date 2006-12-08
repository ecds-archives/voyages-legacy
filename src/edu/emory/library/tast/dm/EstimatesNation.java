package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;

public class EstimatesNation extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "EstimatesNation", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "EstimatesNation"));
	}
	
	private int order;
	
	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public static List loadAllNations()
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = loadAllNations(sess);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List loadAllNations(Session sess)
	{
		return sess.createCriteria(EstimatesNation.class).
		addOrder(Order.asc("name")).
		list();
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static String[] nationNamesToArray(List nations)
	{
		return nationNamesToArray(nations, null);
	}

	public static String[] nationNamesToArray(List nations, Set includeOnly)
	{
		String[] names = new String[nations.size()];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			EstimatesNation nation = (EstimatesNation) iter.next();
			if (includeOnly == null || includeOnly.contains(nation.getId()))
			{
				names[i++] = nation.getName();
			}
		}
		
		return names;
	}

	public static Map createIdIndexMap(List nations)
	{
		Map map = new HashMap();
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			EstimatesNation nation = (EstimatesNation) iter.next();
			map.put(nation.getId(), new Integer(i++));
		}
		
		return map;
	}

	public static EstimatesNation loadById(long nationId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		EstimatesNation nation = loadById(sess, nationId);
		transaction.commit();
		sess.close();
		return nation;
	}

	public static EstimatesNation loadById(Session sess, long nationId)
	{
		List list = sess.createCriteria(EstimatesNation.class).add(Restrictions.eq("id", new Long(nationId))).setCacheable(true).list();
		if (list == null || list.size() == 0) return null;
		return (EstimatesNation) list.get(0);
	}

}