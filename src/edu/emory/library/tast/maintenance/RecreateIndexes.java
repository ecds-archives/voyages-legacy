package edu.emory.library.tast.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import edu.emory.library.tast.util.SqlUtils;
import edu.emory.library.tast.util.StringUtils;

public class RecreateIndexes
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	private static void dropAllIndexes(Connection conn, String table, String[] exceptions) throws SQLException
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
	
	private static void createIndex(Connection conn, String indexName, String table, String column) throws SQLException
	{
		createIndex(conn, indexName, table, new String[] { column }, false, null);
	}

	private static void createIndex(Connection conn, String indexName, String table, String column, boolean unique) throws SQLException
	{
		createIndex(conn, indexName, table, new String[] { column }, unique, null);
	}

	private static void createIndex(Connection conn, String indexName, String table, String column, boolean unique, String method) throws SQLException
	{
		createIndex(conn, indexName, table, new String[] { column }, unique, method);
	}

	private static void createIndex(Connection conn, String indexName, String table, String[] columns, boolean unique, String method) throws SQLException
	{

		System.out.println("Creating index on '" + indexName + "' ...");

		String sql = 
			"CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " " +
			"ON " + table + " " + (method != null ? "USING " + method + " " : "") + 
			"(" + StringUtils.join(", ", columns) + ")";
		
		Statement st = conn.createStatement();
		st.execute(sql);
		
	}
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		dropAllIndexes(conn, "voyages", new String[] {"voyage_iid"});
		
		String[] indexedColumns = new String[] {
				"revision",
				"voyageid",
				"evgreen",
				"shipname",
				"captaina",
				"ownera",
				"national",
				"natinimp",
				"yrcons",
				"yrreg",
				"rig",
				"tonnage",
				"tonmod",
				"guns",
				"fate",
				"fate2",
				"fate3",
				"fate4",
				"resistance",
				"yearam",
				"datedep",
				"datebuy",
				"dateleftafr",
				"dateland1",
				"dateland2",
				"dateland3",
				"datedepam",
				"dateend",
				"voy1imp",
				"voy2imp",
				"crew1",
				"crew3",
				"crewdied",
				"slintend",
				"ncar13",
				"ncar15",
				"ncar17",
				"tslavesd",
				"slaarriv",
				"slas32",
				"slas36",
				"slas39",
				"slaximp",
				"slamimp",
				"menrat7",
				"womrat7",
				"boyrat7",
				"girlrat7",
				"malrat7",
				"chilrat7",
				"jamcaspr",
				"vymrtimp",
				"vymrtrat",
				"sourcea",
				"placcons",
				"placreg",
				"ptdepimp",
				"plac1tra",
				"plac2tra",
				"plac3tra",
				"mjbyptimp",
				"npafttra",
				"sla1port",
				"adpsale1",
				"adpsale2",
				"mjslptimp",
				"portret" };
		
		createIndex(conn, "voyages_index_iid", "voyages", "iid", true);

		for (int i = 0; i < indexedColumns.length; i++)
		{
			createIndex(conn, "voyages_index_" + indexedColumns[i], "voyages", indexedColumns[i]);
		}
		
		createIndex(conn, "voyages_full_text_index_shipname", "voyages", "shipname_index", false, "gin");
		createIndex(conn, "voyages_full_text_index_captains", "voyages", "captains_index", false, "gin");
		createIndex(conn, "voyages_full_text_index_owners", "voyages", "owners_index", false, "gin");
		createIndex(conn, "voyages_full_text_index_sources", "voyages", "sources_index", false, "gin");

		conn.close();
		
	}

}
