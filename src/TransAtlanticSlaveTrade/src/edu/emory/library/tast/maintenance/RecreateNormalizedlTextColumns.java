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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import edu.emory.library.tast.util.SqlUtils;
import edu.emory.library.tast.util.StringUtils;

public class RecreateNormalizedlTextColumns
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://HOSTNAME/DBNAME";
	private static final String DB_USER = "";
	private static final String DB_PASS = "";

	private static final int DB_REVISION = 1;
	
	private static char[] oldChars = "\u00C1\u00C2\u00C3\u00C7\u00C9\u00CD\u00D3\u00DA\u00E1\u00E2\u00E3\u0103\u00E6\u00E7\u010D\u00E8\u00E9\u00EA\u00EB\u0119\u00ED\u00EE\u00EF\u00F1\u00F3\u00F4\u00F5\u00F6\u00FA\u00FC".toCharArray();
	private static char[] newChars = "AAACEIOUaaaaacceeeeeiiinoooouu".toCharArray();
	
	private static void createIndex(Connection conn, int revision, String[] columns, String indexColumn) throws SQLException
	{
		
		System.out.print("Creating index '" + indexColumn + "' ");
		
		int totalKeywords = 0;
		int maxKeywordsPerVoyage = 0;
		
		SqlUtils.dropColumnIfExists(conn, "voyages", indexColumn);
		
		String sqlAddIndexColumn =
			"ALTER TABLE voyages " +
			"ADD COLUMN " + indexColumn + " tsvector" ;
		
		Statement stAddIndexColumn = conn.createStatement();
		stAddIndexColumn.execute(sqlAddIndexColumn);
		
		String sqlInsertKeyword =
			"UPDATE voyages " +
			"SET " + indexColumn + " = CAST(? AS tsvector) " +
			"WHERE iid = ?";
		
		PreparedStatement stSetKeyword = conn.prepareStatement(sqlInsertKeyword);
		
		String sqlVoyages =
			"SELECT iid, " + StringUtils.join(", ", columns) + " " +
			"FROM voyages " +
			"WHERE revision = " + revision;
		
		Statement statementVoyages = conn.createStatement();
		ResultSet rs = statementVoyages.executeQuery(sqlVoyages);
		
		Set uniqueKeywords = new HashSet();
		
		int voyageIndex = 0;
		while (rs.next())
		{
			
			long voyageIid = rs.getLong(1);
			
			uniqueKeywords.clear();
			for (int i = 0; i < columns.length; i++)
			{
				String value = rs.getString(i+2);
				value = StringUtils.replaceChars(value, oldChars, newChars);
				String[] keywords = StringUtils.extractQueryKeywords(value, StringUtils.LOWER_CASE);
				for (int j = 0; j < keywords.length; j++)
				{
					String[] substrings = StringUtils.getAllSubstrings(keywords[j]);
					for (int k = 0; k < substrings.length; k++)
					{
						uniqueKeywords.add(substrings[k]);
					}
				}
			}
			
			if (uniqueKeywords.size() > maxKeywordsPerVoyage)
			{
				maxKeywordsPerVoyage = uniqueKeywords.size(); 
			}
			
			if (uniqueKeywords.size() != 0)
			{
				totalKeywords += uniqueKeywords.size(); 
				String tsquery = StringUtils.join(" ", uniqueKeywords);
				stSetKeyword.setLong(2, voyageIid);
				stSetKeyword.setString(1, tsquery);
				stSetKeyword.addBatch();
			}
			
			if (voyageIndex % 1000 == 0)
			{
				System.out.print(".");
			}
			
			voyageIndex++;
			
		}
		
		stSetKeyword.executeBatch();

		rs.close();
		
		System.out.println();
		System.out.println("Total number of keywords = " + totalKeywords);
		System.out.println("Max number of keywords per voyage = " + maxKeywordsPerVoyage);
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		createIndex(conn, DB_REVISION, new String[] {"shipname"}, "shipname_index");
		createIndex(conn, DB_REVISION, new String[] {"captaina", "captainb", "captainc"}, "captains_index");
		createIndex(conn, DB_REVISION, new String[] {"ownera", "ownerb", "ownerc", "ownerd", "ownere", "ownerf", "ownerg", "ownerh", "owneri", "ownerj", "ownerk", "ownerl", "ownerm", "ownern", "ownero", "ownerp" }, "owners_index");
		createIndex(conn, DB_REVISION, new String[] {"sourcea", "sourceb", "sourcec", "sourced", "sourcee", "sourcef", "sourceg", "sourceh", "sourcei", "sourcej", "sourcek", "sourcel", "sourcem", "sourcen", "sourceo", "sourcep", "sourceq", "sourcer" }, "sources_index");
		
		conn.close();
		
		System.out.println("DONE!");
		
	}

}
