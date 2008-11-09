package edu.emory.library.tast.maintenance.experiments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.emory.library.tast.util.SqlUtils;
import edu.emory.library.tast.util.StringUtils;

public class RecreateLocationColumnsViaTableCopy
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	private static final int DB_REVISION = 1;
	
	private static String createAreaColumnName(String portColumn)
	{
		return portColumn + "_area";
	}

	private static String createRegionColumnName(String portColumn)
	{
		return portColumn + "_region";
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{

		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);

		conn.setAutoCommit(false);
		conn.createStatement().execute("SET CONSTRAINTS DEFERRED");
		
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
		
		System.out.println("Dropping all previous location columns ...");
		
		for (int i = 0; i < portColumns.length; i++)
		{
			
			String portColumn = portColumns[i];
			String regionColumn = createRegionColumnName(portColumn);
			String areaColumn = createAreaColumnName(portColumn);
			
			SqlUtils.dropColumnIfExists(conn, "voyages", regionColumn);
			SqlUtils.dropColumnIfExists(conn, "voyages", areaColumn);
			
		}
		
		System.out.println("Making a copy of voyages with new columns ...");

		ArrayList newColumns = new ArrayList();

		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append("voyages");
		
		for (int i = 0; i < portColumns.length; i++)
		{
			
			String portColumn = portColumns[i];
			String regionColumn = createRegionColumnName(portColumn);
			String areaColumn = createAreaColumnName(portColumn);

			String portsTable = "ports_" + i;
			String regionsTable = "regions_" + i;
			String areasTables = "area_" + i;
		
			newColumns.add(regionsTable + ".id AS " + regionColumn);
			newColumns.add(areasTables + ".id AS " + areaColumn);
			
			sqlFrom.append(" ");
			sqlFrom.append("LEFT JOIN ports ").append(portsTable).append(" ON ");
			sqlFrom.append("voyages").append(".").append(portColumn).append(" = ");
			sqlFrom.append(portsTable).append(".").append("id");
			
			sqlFrom.append(" ");
			sqlFrom.append("LEFT JOIN regions ").append(regionsTable).append(" ON ");
			sqlFrom.append(portsTable).append(".").append("region_id").append(" = ");
			sqlFrom.append(regionsTable).append(".").append("id");
			
			sqlFrom.append(" ");
			sqlFrom.append("LEFT JOIN areas ").append(areasTables).append(" ON ");
			sqlFrom.append(regionsTable).append(".").append("area_id").append(" = ");
			sqlFrom.append(areasTables).append(".").append("id");
			
		}
		
		StringBuilder sqlCopy = new StringBuilder();
		sqlCopy.append("CREATE TABLE voyages_tmp_copy AS ");
		sqlCopy.append("SELECT voyages.*, ").append(StringUtils.join(", ", newColumns)).append(" ");
		sqlCopy.append("FROM ").append(sqlFrom).append(" ");
		sqlCopy.append("WHERE revision = ").append(DB_REVISION);
		
		conn.createStatement().execute(sqlCopy.toString());
		
		System.out.println("Purging the original voyages table ...");
		
		String sqlPurge = "DELETE FROM voyages WHERE revision = 1";
		conn.createStatement().execute(sqlPurge);
		
		System.out.println("Copying voyages back ...");
		
		// not finished ...

		conn.close();
		
	}

}
