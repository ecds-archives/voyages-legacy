package edu.emory.library.tast.dm;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.util.HibernateUtil;

public class Person
{
	
	private int id;
	private String firstName;
	private String middleName;
	private String lastName;
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getMiddleName()
	{
		return middleName;
	}
	
	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}
	
	public static Person loadById(int personId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Person person = loadById(personId, sess);
		transaction.commit();
		sess.close();
		return person;
	}

	public static Person loadById(int personId, Session sess)
	{
		List list = sess.createCriteria(Port.class).add(Restrictions.eq("id", new Integer(personId))).list();
		if (list == null || list.size() == 0) return null;
		return (Person) list.get(0);
	}

	public static List getPeopleList()
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = getPeopleList(sess);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List getPeopleList(Session sess)
	{
		return sess.createCriteria(Region.class).
		addOrder(Order.asc("lastName")).
		addOrder(Order.asc("firstName")).
		addOrder(Order.asc("middleName")).
		list();
	}
	
}
