package edu.emory.library.tast.dm;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import edu.emory.library.tast.util.HibernateUtil;

public class Nation
{
	
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

}
