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
package edu.emory.library.tast.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class SqlUtils
{
	
	public static ArrayList getColumns(Connection conn, String table) throws SQLException
	{
		
		String sql = 	
			"SELECT" +
			"	a.attname " +
			"FROM" +
			"	pg_catalog.pg_stat_user_tables AS t," +
			"	pg_catalog.pg_attribute a " +
			"WHERE" +
			"	a.attnum > 0 AND " +
			"	a.atttypid <> 0 AND " +
			"	t.relid = a.attrelid AND " +
			"	t.schemaname = 'public' AND " +
			"	t.relname = ?";
		
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, table);
		
		ResultSet rs = st.executeQuery();
		ArrayList columns = new ArrayList();
		while (rs.next())
		{
			columns.add(rs.getString(1));
		}
		
		rs.close();
		st.close();
		
		return columns;
		
	}
	
	public static boolean columnExists(Connection conn, String table, String column) throws SQLException
	{
		
		ArrayList columns = getColumns(conn, table);
		for (Iterator iterator = columns.iterator(); iterator.hasNext();)
		{
			String thisColumn = (String) iterator.next();
			if (EqualsUtil.areEqual(thisColumn, column))
			{
				return true;
			}
		}
		
		return false;

	}
	
	public static void dropColumn(Connection conn, String table, String column) throws SQLException
	{
		String sqlDropColumn =
			"ALTER TABLE " + table + " " +
			"DROP COLUMN \"" + column + "\"";
		
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
