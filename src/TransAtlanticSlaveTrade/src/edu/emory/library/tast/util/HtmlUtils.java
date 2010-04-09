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
package edu.emory.library.tast.util;

import java.awt.Color;
import java.io.PrintWriter;

public class HtmlUtils
{

	public static void javaScriptRedirect(PrintWriter out, String URL, boolean showInBody)
	{
		javaScriptRedirect(out, URL, showInBody, 0);
	}

	public static void javaScriptRedirect(PrintWriter out, String URL, boolean showInBody, int parentLevel)
	{
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Redirect</title>");

		out.println("<script language=\"javascript\" type=\"text/javascript\">");
		out.print("window.");
		if (parentLevel == Integer.MAX_VALUE)
		{
			out.print("top.");
		}
		else
		{
			for (int i = 0; i < parentLevel; i++)
			{
				out.print("parent.");
			}
		}
		out.print("location.href = \"" + URL + "\";");
		out.println();
		out.println("</script>");

		out.println("</head>");
		out.println("<body>");
		if (showInBody) out.println("Redirect to: " + URL);
		out.println("</body>");
		out.println("</html>");
	}
	
	public static String formatHtmlColor(Color color)
	{
		return "#" + Integer.toHexString(color.getRGB() & 0x00FFFFFF);
	}
	
}
