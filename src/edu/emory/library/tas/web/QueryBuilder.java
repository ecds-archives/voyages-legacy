package edu.emory.library.tas.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.web.test.Utils;

public class QueryBuilder extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		Utils.setEncodingToUTF8(response);
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		out.println("<html>");
		out.println("<head>");
		out.println("</head>");
		out.println("<body>");
		
		out.println("<b>Hello</b><i>italics ...</i>");

		out.println("</body>");
		out.println("</html>");

	}

}
