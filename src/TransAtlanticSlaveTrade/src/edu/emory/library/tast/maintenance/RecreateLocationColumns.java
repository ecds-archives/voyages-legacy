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

import edu.emory.library.tast.util.SqlUtils;

public class RecreateLocationColumns
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://HOSTNAME/DBNAME";
	private static final String DB_USER = "";
	private static final String DB_PASS = "";
	
	private static final int DB_REVISION = 1;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		long timeStart = System.currentTimeMillis();

		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		String[] portColumns = new String[] {
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
				"portdep",
				"embport",
				"arrport",
				"embport2",
				"arrport2" };
		
		for (int i = 0; i < portColumns.length; i++)
		{
			
			System.out.println("Creating region and area columns for '" + portColumns[i] + "' ...");
			
			String portColumn = portColumns[i] + "_port";
			String regionColumn = portColumns[i] + "_region";
			String areaColumn = portColumns[i] + "_area";
			
			SqlUtils.dropColumnIfExists(conn, "voyages", portColumn);
			SqlUtils.dropColumnIfExists(conn, "voyages", regionColumn);
			SqlUtils.dropColumnIfExists(conn, "voyages", areaColumn);

			String sqlAddPortColumn =
				"ALTER TABLE voyages " +
				"ADD COLUMN " + portColumn + " bigint";
			
			String sqlAddRegionColumn =
				"ALTER TABLE voyages " +
				"ADD COLUMN " + regionColumn + " bigint";
			
			String sqlAddAreaColumn =
				"ALTER TABLE voyages " +
				"ADD COLUMN " + areaColumn + " bigint";
			
			conn.createStatement().execute(sqlAddPortColumn);
			conn.createStatement().execute(sqlAddRegionColumn);
			conn.createStatement().execute(sqlAddAreaColumn);
			
			String sqlPopulateNewColumns =
				"UPDATE " +
				"	voyages " +
				"SET " +
				"	" + portColumn + " = ports.id, " +
				"	" + regionColumn + " = regions.id, " +
				" 	" + areaColumn + " = areas.id " +
				"FROM " +
				"	ports, regions, areas " +
				"WHERE " +
				"	ports.id = voyages." + portColumns[i] + " AND " +
				"	regions.id = ports.region_id AND " +
				"	areas.id = regions.area_id AND " +
				"	voyages.revision = " + DB_REVISION;
			
			conn.createStatement().execute(sqlPopulateNewColumns);
			
		}

		conn.close();
		
		long timeEnd = System.currentTimeMillis();
		System.out.println("Done in " + (timeEnd - timeStart) + " ms.");
		
	}

}
