package edu.emory.library.tast.images.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.common.LookupSource;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.util.StringUtils;

public class LookupSourceRegions implements LookupSource
{

	public boolean canReturnAllItems()
	{
		return true;
	}

	public List allItems()
	{
		return getRegions(null);
	}
	
	public int getMaxLimit()
	{
		return 0;
	}
	
	private List loadRegionsIntoUI(Criteria crit)
	{
		List dbRegions = crit.list();
		List uiRegions = new ArrayList(dbRegions.size());
		for (Iterator iter = dbRegions.iterator(); iter.hasNext();)
		{
			Region dbPerson = (Region) iter.next();
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbPerson.getId()));
			item.setLabel(dbPerson.getName());
			uiRegions.add(item);
		}
		return uiRegions;
	}

	public List getItemsByValues(String[] idsStr)
	{
		
		// nothing to do
		if (idsStr == null || idsStr.length == 0)
			return new ArrayList();
		
		// convert ids to longs
		Long[] ids = StringUtils.parseLongArray(idsStr);

		// open db
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// prepare query
		Criteria crit = sess.createCriteria(Region.class).addOrder(Order.asc("name"));
		crit.add(Restrictions.in("id", ids));
		
		// fill UI list
		List uiRegions = loadRegionsIntoUI(crit);
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiRegions;

	}

	public List search(String value)
	{
		return getRegions(value);
	}
	
	private List getRegions(String searchFor)
	{

		// open db
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();

		// prepare query
		Criteria crit = sess.createCriteria(Region.class).addOrder(Order.asc("name"));
		if (searchFor != null) crit.add(Restrictions.ilike("name", "%" + searchFor + "%"));

		// fill UI list
		List uiRegions = loadRegionsIntoUI(crit);
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiRegions;
		
	}

}