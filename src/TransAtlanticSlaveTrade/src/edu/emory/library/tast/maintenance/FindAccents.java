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
