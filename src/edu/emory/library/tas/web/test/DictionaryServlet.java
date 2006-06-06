package edu.emory.library.tas.web.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class DictionaryServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		Utils.setEncodingToUTF8(response);
		
		PrintWriter out = response.getWriter();
		HtmlWriter html = new HtmlWriter(out);
		html.start("Test");
		
		String []dbNames = Voyage.getAllAttrNames();
		
		html.beginScript();
		out.println("var fields = [");
		int fieldsCount = 0;
		for (int i = 0; i < dbNames.length; i++)
		{
			SchemaColumn col = Voyage.getSchemaColumn(dbNames[i]);
			//out.println("types[\"" + col.getName() + "\"] = {");
			if (fieldsCount > 0) out.println(","); 
			out.print("{");
			out.print("name: \"" + col.getName() + "\", ");
			out.print("label: \"" + col.getName() + "\", ");
			out.print("type: " + col.getType());
			out.print("}");
			fieldsCount++;
		}
		out.println("]");
		html.endScript();
		
		html.addExternalScript("search.js");
		
		html.out.print("<form name=\"search\"><select name=\"fields\"></select></form>");
		
		html.end();

//		Dictionary[] dicts = Dictionary.loadDictionary("EmbRegion");
//		for (int i = 0; i < dicts.length; i++)
//		{
//			writer.println(dicts[i].getId());
//			writer.println(" - ");
//			writer.println(dicts[i].getName());
//			writer.println("<br>");
//		}
		
	}

}
