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
package edu.emory.library.tast.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import edu.emory.library.tast.util.SqlUtils;
import edu.emory.library.tast.util.StringUtils;

public class DropIndexes
{

	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	public static void dropAllIndexes(Connection conn, String table, String[] exceptions) throws SQLException
	{
		
		System.out.println("Deleting all indexes from '" + table + "' ...");
		
		Set exceptionsLookup = StringUtils.toStringSet(exceptions);

		String[] indexes = SqlUtils.getIndexes(conn, table);
		for (int i = 0; i < indexes.length; i++)
		{
			if (!exceptionsLookup.contains(indexes[i]))
			{
				SqlUtils.dropIndex(conn, indexes[i]);
			}
		}
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		dropAllIndexes(conn, "voyages", new String[] {"voyage_iid"});
		
		conn.close();
		
	}

}
