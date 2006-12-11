package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateUtil;

public class Resistance extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Insurrections", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Insurrections"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static Resistance loadById(long insurrectionId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Resistance fate = loadById(sess, insurrectionId);
		transaction.commit();
		sess.close();
		return fate;
	}

	public static Resistance loadById(Session sess, long insurrectionId)
	{
		List list = sess.createCriteria(Resistance.class).add(Restrictions.eq("id", new Long(insurrectionId))).list();
		if (list == null || list.size() == 0) return null;
		return (Resistance) list.get(0);
	}

}