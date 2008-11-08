package edu.emory.library.tast.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import edu.emory.library.tast.util.StringUtils;

public class BuildPosgreSQLFullTextIndexes
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	private static char[] oldChars = "\u00C1\u00C2\u00C3\u00C7\u00C9\u00CD\u00D3\u00DA\u00E1\u00E2\u00E3\u0103\u00E6\u00E7\u010D\u00E8\u00E9\u00EA\u00EB\u0119\u00ED\u00EE\u00EF\u00F1\u00F3\u00F4\u00F5\u00F6\u00FA\u00FC".toCharArray();
	private static char[] newChars = "AAACEIOUaaaaacceeeeeiiinoooouu".toCharArray();
	
	private static boolean checkColumnExists(Connection conn, String table, String column) throws SQLException
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
	
	private static void createIndex(Connection conn, int revision, String[] columns, String indexColumn) throws SQLException
	{
		
		System.out.print("Creating index '" + indexColumn + "' ");
		
		int totalKeywords = 0;
		int maxKeywordsPerVoyage = 0;
		
		if (checkColumnExists(conn, "voyages", indexColumn))
		{
			
			String sqlDropIndexColumn =
				"ALTER TABLE voyages " +
				"DROP COLUMN " + indexColumn;
			
			Statement statementDropColumn = conn.createStatement();
			statementDropColumn.execute(sqlDropIndexColumn);
			
		}
		
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
		
		String sqlAddSqlIndex =
			"CREATE INDEX " + indexColumn + " " + 
			"ON voyages USING gin(" + indexColumn + ")";
		
		Statement statementAddSqlIndex = conn.createStatement();
		statementAddSqlIndex.execute(sqlAddSqlIndex);
		
		System.out.println();
		System.out.println("Total number of keywords = " + totalKeywords);
		System.out.println("Max number of keywords per voyage = " + maxKeywordsPerVoyage);
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		int revision = 1;
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		createIndex(conn, revision, new String[] {"shipname"}, "shipname_index");
		createIndex(conn, revision, new String[] {"captaina", "captainb", "captainc"}, "captains_index");
		createIndex(conn, revision, new String[] {"ownera", "ownerb", "ownerc", "ownerd", "ownere", "ownerf", "ownerg", "ownerh", "owneri", "ownerj", "ownerk", "ownerl", "ownerm", "ownern", "ownero", "ownerp" }, "owners_index");
		createIndex(conn, revision, new String[] {"sourcea", "sourceb", "sourcec", "sourced", "sourcee", "sourcef", "sourceg", "sourceh", "sourcei", "sourcej", "sourcek", "sourcel", "sourcem", "sourcen", "sourceo", "sourcep", "sourceq", "sourcer" }, "sources_index");
		
		conn.close();
		
	}

}
