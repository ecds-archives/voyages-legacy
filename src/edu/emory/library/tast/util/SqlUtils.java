package edu.emory.library.tast.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlUtils
{
	
	public static boolean columnExists(Connection conn, String table, String column) throws SQLException
	{
		
		String sql = 	
			"SELECT" +
			"	count(a.attname) " +
			"FROM" +
			"	pg_catalog.pg_stat_user_tables AS t," +
			"	pg_catalog.pg_attribute a " +
			"WHERE" +
			"	t.relid = a.attrelid AND " +
			"	t.schemaname = 'public' AND " +
			"	t.relname = ? AND " +
			"	a.attname = ?";
		
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, table);
		st.setString(2, column);
		
		ResultSet rs = st.executeQuery();
		rs.next();
		boolean exists = rs.getLong(1) != 0;
		
		rs.close();
		st.close();
		
		return exists;
		
	}
	
	public static void dropColumn(Connection conn, String table, String column) throws SQLException
	{
		String sqlDropColumn =
			"ALTER TABLE " + table + " " +
			"DROP COLUMN " + column;
		
		Statement statementDropColumn = conn.createStatement();
		statementDropColumn.execute(sqlDropColumn);
	}
	
	public static void dropColumnIfExists(Connection conn, String table, String column) throws SQLException
	{
		if (columnExists(conn, table, column))
		{
			dropColumn(conn, table, column);
		}
	}
	
	public static String[] getIndexes(Connection conn, String table) throws SQLException
	{

		String sql = 	
			"SELECT" +
			"	indexname " +
			"FROM" +
			"	pg_catalog.pg_indexes " +
			"WHERE" +
			"	tablename = ?";
		
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, table);
		
		ArrayList indexes = new ArrayList();
		ResultSet rs = st.executeQuery();
		while (rs.next())
		{
			indexes.add(rs.getString(1));
		}
		
		rs.close();
		st.close();
		
		String[] indexesArr = new String[indexes.size()];
		return (String[]) indexes.toArray(indexesArr);
		
	}
	
	public static void dropIndex(Connection conn, String index) throws SQLException
	{
		String sqlDropIndexColumn =
			"DROP INDEX " + index;
		
		Statement statementDropIndex = conn.createStatement();
		statementDropIndex.execute(sqlDropIndexColumn);
	}	

}
