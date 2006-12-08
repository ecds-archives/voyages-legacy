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

public class FateOwner extends Dictionary
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Fate", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Fate"));
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static FateOwner loadById(long fateId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		FateOwner fate = loadById(sess, fateId);
		transaction.commit();
		sess.close();
		return fate;
	}

	public static FateOwner loadById(Session sess, long fateId)
	{
		List list = sess.createCriteria(FateOwner.class).add(Restrictions.eq("id", new Long(fateId))).list();
		if (list == null || list.size() == 0) return null;
		return (FateOwner) list.get(0);
	}

}