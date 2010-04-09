/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.misc.tests.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;

public class QueryTest
{

	public static void thisAlwaysUsesSelects()
	{
		
		Session session = HibernateConn.getSession();
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
		
		Session session = HibernateConn.getSession();
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
		
		Session session = HibernateConn.getSession();
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
		
		//Voyage[] voyages = null;
		
		long startTime = System.currentTimeMillis();
		
		//int pageSize = 100;
		//int firstRecord = 0;
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