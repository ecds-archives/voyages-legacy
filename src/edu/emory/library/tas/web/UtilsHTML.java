package edu.emory.library.tas.web;

import java.io.PrintWriter;

public class UtilsHTML
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
	
}
