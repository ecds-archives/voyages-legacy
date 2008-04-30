package edu.emory.library.tast.maintenance;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanupImages
{
	
	private static final String IMAGES_DIR = "F:\\Emory\\TAST\\projects\\tast\\images-database";

	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";

	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{

		Class.forName("org.postgresql.Driver");
		
		Pattern fileNameRegEx = Pattern.compile("([a-zA-Z0-9]+)\\.([a-zA-Z0-9]+)");		
		
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		Statement st = conn.createStatement();
		
		File imagesDir = new File(IMAGES_DIR);
		File[] images = imagesDir.listFiles();
		
		int deleted = 0;
		int deleteFailures = 0;
		
		for (int i = 0; i < images.length; i++)
		{
			File img = images[i];
			
			boolean found = false;
			
			Matcher matcher = fileNameRegEx.matcher(img.getName());
			
			if (matcher.matches())
			{
				
				String sql =
					"SELECT COUNT(*) " +
					"FROM images " +
					"WHERE file_name = '" + img.getName() + "'";
				
				ResultSet rs = st.executeQuery(sql);
				if (rs.next()) found = rs.getLong(1) != 0;
				rs.close();
				
			}
			
			if (!found)
			{
				boolean delete_ok = img.delete(); 
				System.out.print("Deleting " + img.getName());
				if (!delete_ok) System.out.print(" (deletion failed!)");
				System.out.println();
				if (delete_ok) deleted++; else deleteFailures++;
			}
		
		}
		
		st.close();
		conn.close();
		
		System.out.println("------------------------------------------");
		System.out.println("Total deleted     : " + deleted);
		System.out.println("Deletion failures : " + deleteFailures);
		System.out.println("Total images      : " + images.length);
		System.out.println("------------------------------------------");
		
	}

}
