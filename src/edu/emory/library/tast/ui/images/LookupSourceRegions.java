package edu.emory.library.tast.ui.images;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.LookupSource;
import edu.emory.library.tast.util.HibernateUtil;

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

	public List getItemsByValues(String[] ids)
	{

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// prepare the list
		List uiRegions = new ArrayList(ids.length);

		// load one by one
		for (int i = 0; i < ids.length; i++)
		{
			int regionId = Integer.parseInt(ids[i]);
			Region dbRegion = Region.loadById(sess, regionId);
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbRegion.getId()));
			item.setLabel(dbRegion.getName());
			uiRegions.add(item);
		}
		
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
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load all regions
		if (searchFor != null) searchFor = "%" + searchFor + "%";
		List dbRegions = Region.loadRegions(sess, searchFor);
		List uiRegions = new ArrayList(dbRegions.size());

		// fill UI list
		for (Iterator iter = dbRegions.iterator(); iter.hasNext();)
		{
			Region dbPerson = (Region) iter.next();
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbPerson.getId()));
			item.setLabel(dbPerson.getName());
			uiRegions.add(item);
		}
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiRegions;
		
	}

}