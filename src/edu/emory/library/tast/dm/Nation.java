package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class Nation
{

	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", "Nation"));
		attributes.put("name", new StringAttribute("name", "Nation"));
	}
	
	private int id;
	private String name;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
		return sess.createCriteria(Nation.class).
		addOrder(Order.asc("name")).
		list();
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static String[] nationNamesToArray(List nations)
	{
		String[] names = new String[nations.size()];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			names[i++] = nation.getName();
		}
		
		return names;
	}

	public static Map createIdIndexMap(List nations)
	{
		Map map = new HashMap();
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			map.put(new Integer(nation.getId()), new Integer(i++));
		}
		
		return map;
	}

	public static Nation loadById(int id) {
		Conditions cond = new Conditions();
		cond.addCondition(Nation.getAttribute("id"), new Integer(id), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue(new String[] {"edu.emory.library.tast.dm.Nation"}, new String[] {"n"}, cond);
		Object[] ret = qValue.executeQuery();
		if (ret.length > 0) {
			return (Nation)ret[0];
		}
		return null;
	}

}