package edu.emory.library.tast.maintenance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class FindAccents
{
	
	private static char goodChars[] = "abcdefghijklmnopqrtsuvwxyzABCDEFGHIJKLMNOPQRTSUVWXYZ0123456789()_'-,?.&/[]\" /\\*’¡+".toCharArray();
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";
	
	
	private static boolean isAscii(char ch)
	{
		for (int k = 0; k < goodChars.length; k++)
		{
			if (ch == goodChars[k])
			{
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException
	{
		
		Class.forName("org.postgresql.Driver");
		
		String cols[] = new String[] {
			"shipname",
			"captaina",
			"captainb",
			"captainc",
			"ownera",
			"ownerb",
			"ownerc",
			"ownerd",
			"ownere",
			"ownerf",
			"ownerg",
			"ownerh",
			"owneri",
			"ownerj",
			"ownerk",
			"ownerl",
			"ownerm",
			"ownern",
			"ownero",
			"ownerp"};
		
		Set nonAscii = new TreeSet();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		for (int i = 0; i < cols.length; i++)
		{
			if (i > 0) sql.append(", ");
			sql.append(cols[i]);
		}
		sql.append(" FROM voyages");

		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql.toString());
		
		while (rs.next())
		{
			for (int i = 0; i < cols.length; i++)
			{
				String str = rs.getString(cols[i]);
				if (str != null)
				{
					char chars[] = str.toCharArray();
					for (int j = 0; j < chars.length; j++)
					{
						if (!isAscii(chars[j]))
						{
							nonAscii.add(new Character(chars[j]));
						}
					}
				}
			}
		}
		
		rs.close();
		conn.close();
		
		FileOutputStream f = new FileOutputStream("chars.txt");
		OutputStreamWriter fw = new OutputStreamWriter(f, "UTF8");

		for (Iterator iter = nonAscii.iterator(); iter.hasNext();)
		{
			Character ch = (Character) iter.next();
			fw.write(new char[] {ch.charValue()});
		}
		
		fw.close();
		f.close();
		
	}

}
