package edu.emory.library.tast.ui.images.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.ui.LookupSource;
import edu.emory.library.tast.util.HibernateUtil;

public class LookupSourcePorts implements LookupSource
{

	public boolean canReturnAllItems()
	{
		return true;
	}

	public List allItems()
	{
		return getPorts(null);
	}

	public int getMaxLimit()
	{
		return 0;
	}
	
	public List getItemsByValues(String[] ids)
	{

		// nothing to do
		if (ids == null)
			return new ArrayList();

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// prepare the list
		List uiPorts = new ArrayList(ids.length);

		// load one by one
		for (int i = 0; i < ids.length; i++)
		{
			int portId = Integer.parseInt(ids[i]);
			Port dbPort = Port.loadById(sess, portId);
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbPort.getId()));
			item.setLabel(dbPort.getName());
			uiPorts.add(item);
		}
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiPorts;

	}

	public List search(String value)
	{
		return getPorts(value);
	}
	
	private List getPorts(String searchFor)
	{

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load all ports
		if (searchFor != null) searchFor = "%" + searchFor + "%";
		List dbPorts = Port.loadPorts(sess, searchFor);
		List uiPorts = new ArrayList(dbPorts.size());

		// fill UI list
		for (Iterator iter = dbPorts.iterator(); iter.hasNext();)
		{
			Port dbPort = (Port) iter.next();
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbPort.getId()));
			item.setLabel(dbPort.getRegion().getName() + " / " + dbPort.getName());
			uiPorts.add(item);
		}
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiPorts;
		
	}

}