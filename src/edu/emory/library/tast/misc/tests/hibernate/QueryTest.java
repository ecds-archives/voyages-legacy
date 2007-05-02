package edu.emory.library.tast.misc.tests.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.util.HibernateUtil;

public class QueryTest
{

	public static void thisAlwaysUsesSelects()
	{
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from Book");
		
		List list = query.list();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Book b = (Book) iter.next();
			System.out.println(b.getAuthor().getName());
		}

		transaction.commit();

	}

	public static void avoidingSelectsInCode()
	{
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		List list = session.createCriteria(Book.class).setFetchMode("permissions", FetchMode.JOIN).list();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Book b = (Book) iter.next();
			System.out.println(b.getAuthor().getName());
		}
		
		transaction.commit();
		
	}
	
	public static void avoidingSelectsInHSQL()
	{
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from Book as book left join fetch book.author as a");
		
		List list = query.list();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Book b = (Book) iter.next();
			System.out.println(b.getAuthor().getName());
		}
		
		transaction.commit();
		
	}

	public static void main(String[] args)
	{
		
		Voyage[] voyages = null;
		
		long startTime = System.currentTimeMillis();
		
		int pageSize = 100;
		int firstRecord = 0;
//		while ((voyages = Voyage.loadAllMostRecent(firstRecord, pageSize)).length > 0)
//		{
//			for (int i = 0; i < voyages.length; i++)
//			{
//				System.out.println((firstRecord+i+1) + ". " + voyages[i].getShipname());
//			}
//			firstRecord += voyages.length;
//			if (firstRecord >= 1000) break;
//		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total time = " + (endTime - startTime) / 1000 + " seconds.");

//		Session session = HibernateUtil.getSession();
//		Transaction transaction = session.beginTransaction();
//		Query query = session.createQuery("from VoyageIndex order by vid, global_rev_id");
//		
//		List list = query.list();
//		
//		for (Iterator iter = list.iterator(); iter.hasNext();)
//		{
//			VoyageIndex vi = (VoyageIndex) iter.next();
//			System.out.println(vi.getVoyage().getShipname());
//		}
//		
//		transaction.commit();
		
		avoidingSelectsInCode();
		avoidingSelectsInHSQL();

	}

}