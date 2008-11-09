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
