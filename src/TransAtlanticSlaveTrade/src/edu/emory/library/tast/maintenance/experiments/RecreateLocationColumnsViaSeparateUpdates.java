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
package edu.emory.library.tast.maintenance.experiments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.emory.library.tast.util.SqlUtils;

/**
 * Jan Zich, 9/11/2008
 * Suprisingly, this seems to be a bit more efficient that
 * {@link RecreateLocationColumnsViaSingleUpdate}, but it's not still fast enough.
 */

public class RecreateLocationColumnsViaSeparateUpdates
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
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
			
			System.out.print("Creating region and area columns for '" + portColumns[i] + "' ");
			
			String portColumn = portColumns[i];
			String regionColumn = portColumn + "_region";
			String areaColumn = portColumn + "_area";
			
			SqlUtils.dropColumnIfExists(conn, "voyages", regionColumn);
			SqlUtils.dropColumnIfExists(conn, "voyages", areaColumn);
			
			String sqlAddRegionColumn =
				"ALTER TABLE voyages " +
				"ADD COLUMN " + regionColumn + " bigint";
			
			Statement stAddRegionColumn = conn.createStatement();
			stAddRegionColumn.execute(sqlAddRegionColumn);
			
			String sqlAddAreaColumn =
				"ALTER TABLE voyages " +
				"ADD COLUMN " + areaColumn + " bigint";
			
			Statement stAddAreaColumn = conn.createStatement();
			stAddAreaColumn.execute(sqlAddAreaColumn);
			
			String sqlUpdateVoyage =
				"UPDATE voyages " +
				"SET " + regionColumn + " = ?, " + areaColumn + " = ? " +
				"WHERE iid = ?";
			
			PreparedStatement stUpdateVoyage = conn.prepareStatement(sqlUpdateVoyage);
			
			String sqlVoyages =
				"SELECT " +
				"	regions.id, " +
				"	areas.id, " +
				"	iid " +
				"FROM " +
				"	voyages" +
				"	INNER JOIN ports ON voyages." + portColumn + " = ports.id " +
				"	INNER JOIN regions ON ports.region_id = regions.id " +
				"	INNER JOIN areas ON regions.area_id = areas.id " +
				"WHERE " +
				"	revision = " + DB_REVISION;
			
			Statement statementVoyages = conn.createStatement();
			ResultSet rs = statementVoyages.executeQuery(sqlVoyages);
			
			int voyageIndex = 0;
			while (rs.next())
			{
				
				stUpdateVoyage.setLong(1, rs.getLong(1));
				stUpdateVoyage.setLong(2, rs.getLong(2));
				stUpdateVoyage.setLong(3, rs.getLong(3));
				stUpdateVoyage.addBatch();
				
				if (voyageIndex % 1000 == 0)
				{
					System.out.print(".");
				}
				
				voyageIndex++;				
			}
			
			stUpdateVoyage.executeBatch();
			rs.close();
			
			System.out.println();
			
		}

		conn.close();
		
		long timeEnd = System.currentTimeMillis();
		System.out.println("Done in " + (timeEnd - timeStart) + " ms.");
		
	}

}
