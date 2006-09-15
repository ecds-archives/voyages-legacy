package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;

public class Person
{
	
	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new StringAttribute("id", "Person"));
		attributes.put("firstName", new StringAttribute("firstName", "Person"));
		attributes.put("middleName", new StringAttribute("description", "Person"));
		attributes.put("lastName", new StringAttribute("lastName", "Person"));
	}

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
		Person person = loadById(sess, personId);
		transaction.commit();
		sess.close();
		return person;
	}

	public static Person loadById(Session sess, int personId)
	{
		List list = sess.createCriteria(Person.class).add(Restrictions.eq("id", new Integer(personId))).list();
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
		return sess.createCriteria(Person.class).
		addOrder(Order.asc("lastName")).
		addOrder(Order.asc("firstName")).
		addOrder(Order.asc("middleName")).
		list();
	}
	
	public static List getPeopleList(String substring)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		List list = getPeopleList(sess, substring, -1);
		transaction.commit();
		sess.close();
		return list;
	}
	
	public static List getPeopleList(Session sess, String substring, int maxResults)
	{
		
		Criteria crit = sess.createCriteria(Person.class).
		addOrder(Order.asc("lastName")).
		addOrder(Order.asc("firstName")).
		addOrder(Order.asc("middleName"));
		
		if (!StringUtils.isNullOrEmpty(substring))
			crit.add(Restrictions.ilike("lastName", substring));
		
		if (maxResults >= 0)
			crit.setMaxResults(maxResults);
		
		return crit.list();
	}

	public int hashCode()
	{
		return id;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (this.firstName != null) {
			buffer.append(this.firstName).append(" ");
		}
		if (this.middleName != null) {
			buffer.append(this.middleName).append(" ");
		}
		if (this.lastName != null) {
			buffer.append(this.lastName).append(" ");
		}
		return buffer.toString().trim();
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}
