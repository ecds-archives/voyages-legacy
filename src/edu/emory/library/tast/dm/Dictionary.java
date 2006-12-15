package edu.emory.library.tast.dm;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.util.HibernateUtil;



/**
 * Superclass for any dictionary in the application.
 * @author Pawel Jurczyk
 *
 */
public abstract class Dictionary 
{

	private Long id;
	private String name;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object that)
	{
		if (that instanceof Dictionary)
		{
			Dictionary dict = (Dictionary)that;
			return dict.getId().equals(this.getId());
		}
		return false;
	}
	
	public int hashCode()
	{
		return id.hashCode();
	}
	
	public static List loadAll(Class clazz, Session sess)
	{
		return loadAll(clazz, sess, "name");
	}
	
	public static List loadAll(Class clazz, Session sess, String orderBy)
	{

		boolean sessionProvided = sess != null;
		Transaction transaction = null;
		if (!sessionProvided)
		{
			sess = HibernateUtil.getSession();
			transaction = sess.beginTransaction();
		}
		
		List rigs = sess.createCriteria(clazz).addOrder(Order.asc(orderBy)).list();
		
		if (!sessionProvided)
		{
			transaction.commit();
			sess.close();
		}
		
		return rigs;

	}

	public static Object loadById(Class clazz, Session sess, long id)
	{

		boolean sessionProvided = sess != null;
		Transaction transaction = null;
		if (!sessionProvided)
		{
			sess = HibernateUtil.getSession();
			transaction = sess.beginTransaction();
		}
		//System.out.println(clazz + " id: " + id);
		List list = sess.createCriteria(clazz).add(Restrictions.eq("id", new Long(id))).setCacheable(true).list();
		Object dictItem = null;
		if (list == null || list.size() == 0)
		{
			dictItem = null;
		}
		else
		{
			dictItem = list.get(0);
		}

		if (!sessionProvided)
		{
			transaction.commit();
			sess.close();
		}

		return dictItem;

	}
	
}