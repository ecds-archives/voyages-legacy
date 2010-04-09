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
