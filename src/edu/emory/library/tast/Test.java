package edu.emory.library.tast;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.db.HibernateConn;

public class Test
{

	public static void main(String[] args)
	{
		
		String sql =
			"SELECT " +
			"date_part('year', datedep), " +
			"COUNT(slaximp > 0 AND resistance > 0) / COUNT(slaximp > 0)" +
			"FROM voyages " +
			"GROUP BY date_part('year', datedep) " +
			"ORDER BY date_part('year', datedep) ASC";
		
		System.out.println(sql);
		
		Session sess = HibernateConn.getSession();
		Transaction tran = sess.beginTransaction();
		
		sess.createSQLQuery(sql);

		tran.commit();
		sess.close();
		
	}

}
