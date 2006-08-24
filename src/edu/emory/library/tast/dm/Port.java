package edu.emory.library.tast.dm;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.util.HibernateUtil;

public class Port extends Location
{
	
	private Region region;

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}
	
	public static Port loadById(int portId)
	{
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		Port port = loadById(portId, sess);
		transaction.commit();
		sess.close();
		return port;
	}

	public static Port loadById(int portId, Session sess)
	{
		List list = sess.createCriteria(Port.class).add(Restrictions.eq("id", new Integer(portId))).list();
		if (list == null || list.size() == 0) return null;
		return (Port) list.get(0);
	}

}
