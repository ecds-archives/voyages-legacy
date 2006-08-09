package edu.emory.library.tast.ui.search.query.searchables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Export
{
	
	private static void exportAttributes() throws SQLException
	{
		
		String sql = 
			"SELECT * FROM (" +
			"SELECT " +
			"iid, " +
			"name AS id, " +
			"user_label AS userLabel, " +
			"CASE WHEN category = 0 THEN 'beginners' ELSE 'general' END AS userCategory, " +
			"1 AS compound " +
			"FROM compound_attributes where visible " +
			"UNION " +
			"SELECT " +
			"iid, " +
			"attr_name AS id, " +
			"user_label AS userLabel, " +
			"CASE WHEN attr_category = 0 THEN 'beginners' ELSE 'general' END AS userCategory, " +
			"0 AS compound " +
			"FROM attributes where attr_visible " +
			") allAttributes " +
			"ORDER BY allAttributes.userLabel";
		
		Connection connPrm = DriverManager.getConnection("jdbc:postgresql://wilson.library.emory.edu/tasdb", "tasuser", "tasuser");
		Connection connSec = DriverManager.getConnection("jdbc:postgresql://wilson.library.emory.edu/tasdb", "tasuser", "tasuser");
		
		Statement st = connPrm.createStatement();
		ResultSet rs = st.executeQuery(sql);

		System.out.println("<searchable-attributes>");
		while (rs.next())
		{
			
			int iid = rs.getInt("iid");
			String id = rs.getString("id");
			String userLabel = rs.getString("userLabel");
			String userCategory = rs.getString("userCategory");
			boolean isCompound = rs.getBoolean("compound");
			
			System.out.println("\t<searchable-attribute type=\"simple\" id=\"" + id + "\" userCategory=\"" + userCategory + "\" userLabel=\"" + userLabel + "\">");
			System.out.println("\t\t<attributes>");
			if (!isCompound)
			{
				System.out.println("\t\t\t<attribute name=\"" + id + "\"/>");
			}
			else
			{
				Statement stBasicAttrs = connSec.createStatement();
				ResultSet rsBasicAttrs = stBasicAttrs.executeQuery("SELECT attributes.attr_name AS id from attributes INNER JOIN compound_attribute_attributes ON attributes.iid=compound_attribute_attributes.r_attribute_id WHERE compound_attribute_attributes.r_compound_attribute_id = " + iid);
				while (rsBasicAttrs.next())
				{
					System.out.println("\t\t\t<attribute name=\"" + rsBasicAttrs.getString("id") + "\"/>");
				}
			}
			System.out.println("\t\t</attributes>");
			System.out.println("\t</searchable-attribute>");

		}
		System.out.println("</searchable-attributes>");
		
		rs.close();
		
		connPrm.close();
		
	}
	
	private static void exportGroups() throws SQLException
	{
		
		String sql = 
			"SELECT " +
			"iid, " +
			"name AS id, " +
			"user_label AS userLabel " +
			"FROM groups " +
			"ORDER BY groups.user_label";
		
		Connection connPrm = DriverManager.getConnection("jdbc:postgresql://wilson.library.emory.edu/tasdb", "tasuser", "tasuser");
		Connection connSec = DriverManager.getConnection("jdbc:postgresql://wilson.library.emory.edu/tasdb", "tasuser", "tasuser");
		
		Statement st = connPrm.createStatement();
		ResultSet rs = st.executeQuery(sql);

		System.out.println("<groups>");
		while (rs.next())
		{
			
			int iid = rs.getInt("iid");
			String id = rs.getString("id");
			String userLabel = rs.getString("userLabel");
			
			System.out.println("\t<group id=\"" + id + "\" userLabel=\"" + userLabel + "\">");
			
			Statement stAttrs = connSec.createStatement();
			ResultSet rsAttrs = stAttrs.executeQuery("SELECT attributes.attr_name AS id from attributes INNER JOIN group_attributes ON attributes.iid=group_attributes.r_attribute_id WHERE group_attributes.r_group_id = " + iid);
			while (rsAttrs.next())
			{
				System.out.println("\t\t<searchable-attribute id=\"" + rsAttrs.getString("id") + "\"/>");
			}
			rsAttrs.close();
			
			Statement stCompAttrs = connSec.createStatement();
			ResultSet rsCompAttrs = stCompAttrs.executeQuery("SELECT compound_attributes.name AS id from compound_attributes INNER JOIN group_compound_attributes ON compound_attributes.iid=group_compound_attributes.r_compound_attribute_id WHERE group_compound_attributes.r_group_id = " + iid);
			while (rsCompAttrs.next())
			{
				System.out.println("\t\t<searchable-attribute id=\"" + rsCompAttrs.getString("id") + "\"/>");
			}
			rsCompAttrs.close();

			System.out.println("\t</group>");

		}
		System.out.println("</groups>");
		
		rs.close();
		
		connPrm.close();
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.postgresql.Driver");
		exportAttributes();
		exportGroups();
	}

}
