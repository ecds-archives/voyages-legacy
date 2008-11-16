package edu.emory.library.tast.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.emory.library.tast.util.EqualsUtil;
import edu.emory.library.tast.util.SqlUtils;

public class DropUnusedColumns
{
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		
		String[] columnsToKeep = new String[] {
				"iid",
				"suggestion",
				"revision",
				"voyageid",
				"evgreen",
				"shipname",
				"national",
				"natinimp",
				"yrcons",
				"yrreg",
				"rig",
				"tonnage",
				"tonmod",
				"guns",
				"ownera",
				"ownerf",
				"ownerb",
				"ownerc",
				"ownerd",
				"ownere",
				"ownerg",
				"ownerh",
				"owneri",
				"ownerj",
				"ownerk",
				"ownerl",
				"ownerm",
				"ownern",
				"ownero",
				"ownerp",
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
				"captaina",
				"captainb",
				"captainc",
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
				"sourceg",
				"sourceh",
				"sourcei",
				"sourcej",
				"sourcek",
				"sourcel",
				"sourcem",
				"sourcen",
				"sourceo",
				"sourcep",
				"sourceq",
				"sourcer",
				"sourcea",
				"sourceb",
				"sourcec",
				"sourced",
				"sourcee",
				"sourcef",
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
				"deptregimp",
				"regem1",
				"regem2",
				"regem3",
				"majbyimp",
				"regdis1",
				"regdis2",
				"regdis3",
				"mjselimp",
				"retrnreg",
				"embport",
				"embport2",
				"arrport",
				"arrport2",
				/*
				"shipname_index",
				"captains_index",
				"owners_index",
				"sources_index",
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
				"arrport2_area",
				*/
		};
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		long timeStart = System.currentTimeMillis();
		
		ArrayList existingColumns = SqlUtils.getColumns(conn, "voyages");
		for (Iterator iterator = existingColumns.iterator(); iterator.hasNext();)
		{
			String existingColumn = (String) iterator.next();
			boolean keep = false;
			for (int i = 0; i < columnsToKeep.length && !keep; i++)
			{
				if (EqualsUtil.areEqual(existingColumn, columnsToKeep[i]))
				{
					keep = true;
				}
			}
			if (!keep)
			{
				System.out.println("Removing column '" + existingColumn + "' ...");
				SqlUtils.dropColumn(conn, "voyages", existingColumn);
			}
		}
		
		long timeEnd = System.currentTimeMillis();
		System.out.println("Done in " + (timeEnd - timeStart) + " ms.");

		conn.close();
		
	}

}
