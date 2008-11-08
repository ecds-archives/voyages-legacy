package edu.emory.library.tast.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RecreateRegularIndexes
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	private static void dropAllIndexes(Connection conn, String table)
	{
		
		// "SELECT indexrelname FROM pg_catalog.pg_stat_user_indexes WHERE relname = ?";
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		int revision = 1;
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		conn.close();
		
	}

}
