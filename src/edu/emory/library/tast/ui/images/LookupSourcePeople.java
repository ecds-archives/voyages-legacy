package edu.emory.library.tast.ui.images;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.ui.LookupSource;
import edu.emory.library.tast.util.HibernateUtil;

public class LookupSourcePeople implements LookupSource
{

	public boolean canReturnAllItems()
	{
		return false;
	}

	public List allItems()
	{
		return null;
	}

	public int getMaxLimit()
	{
		return 100;
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
		List uiPeople = new ArrayList(ids.length);

		// load one by one
		for (int i = 0; i < ids.length; i++)
		{
			int personId = Integer.parseInt(ids[i]);
			Person dbPerson = Person.loadById(sess, personId);
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbPerson.getId()));
			item.setLabel(dbPerson.getLastName());
			uiPeople.add(item);
		}
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiPeople;

	}

	public List search(String value)
	{

		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load all people
		List dbPeople = Person.getPeopleList(sess, "%" + value + "%", getMaxLimit());
		List uiPeople = new ArrayList(dbPeople.size());

		// fill people list
		for (Iterator iter = dbPeople.iterator(); iter.hasNext();)
		{
			Person dbPerson = (Person) iter.next();
			SelectItem item = new SelectItem();
			item.setValue(String.valueOf(dbPerson.getId()));
			item.setLabel(dbPerson.getLastName());
			uiPeople.add(item);
		}
		
		// close db
		transaction.commit();
		sess.close();
		
		// return
		return uiPeople;

	}

}