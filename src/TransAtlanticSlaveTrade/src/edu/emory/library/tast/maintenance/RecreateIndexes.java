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
import java.sql.Statement;

import edu.emory.library.tast.util.StringUtils;

public class RecreateIndexes
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
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
		
		long timeStart = System.currentTimeMillis();
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		DropIndexes.dropAllIndexes(conn, "voyages", new String[] {"voyage_iid"});
		
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
				"portret",
				"placcons_region",
				"placcons_area",
				"placreg_region",
				"placreg_area",
				"ptdepimp_region",
				"ptdepimp_area",
				"plac1tra_region",
				"plac1tra_area",
				"plac2tra_region",
				"plac2tra_area",
				"plac3tra_region",
				"plac3tra_area",
				"mjbyptimp_region",
				"mjbyptimp_area",
				"npafttra_region",
				"npafttra_area",
				"sla1port_region",
				"sla1port_area",
				"adpsale1_region",
				"adpsale1_area",
				"adpsale2_region",
				"adpsale2_area",
				"mjslptimp_region",
				"mjslptimp_area",
				"portret_region",
				"portret_area",
				"portdep_region",
				"portdep_area",
				"embport_region",
				"embport_area",
				"arrport_region",
				"arrport_area",
				"embport2_region",
				"embport2_area",
				"arrport2_region",
				"arrport2_area" };

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
		
		long timeEnd = System.currentTimeMillis();
		System.out.println("Done in " + (timeEnd - timeStart) + " ms.");

	}

}
