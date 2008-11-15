package edu.emory.library.tast.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.emory.library.tast.util.SqlUtils;

public class RecreateLocationColumns
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
			
			System.out.println("Creating region and area columns for '" + portColumns[i] + "' ...");
			
			String portColumn = portColumns[i] + "_port";
			String regionColumn = portColumn + "_region";
			String areaColumn = portColumn + "_area";
			
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
