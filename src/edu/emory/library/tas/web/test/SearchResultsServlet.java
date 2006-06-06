package edu.emory.library.tas.web.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class SearchResultsServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		PrintWriter out = response.getWriter();
		
		String[] dbNames = Voyage.getAllAttrNames();
		String[] selectedConditions = request.getParameterValues("selected-conditions");
		
		for (int i = 0; i < selectedConditions.length; i++)
		{
			String cond = selectedConditions[i];
			SchemaColumn col = Voyage.getSchemaColumn(cond);
			
			switch (col.getType())
			{
				case SchemaColumn.TYPE_STRING:
					out.println(
							"String " +
							col.getName() + " = " +
							request.getParameter(col.getName()));
					break;
				
				case SchemaColumn.TYPE_INTEGER:
				case SchemaColumn.TYPE_LONG:
				case SchemaColumn.TYPE_DATE:
//					if ("range".equals(request.getParameter(col.getName() + """))))
//					{
//						
//						
//					}
					break;

				case SchemaColumn.TYPE_DICT:
					out.println(
							"Dictionary " +
							col.getName() + " = " +
							request.getParameter(col.getName()));
					break;

			}
			
		}
		
	}

}
