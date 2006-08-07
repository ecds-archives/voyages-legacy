package edu.emory.library.tast.web.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Voyage;

public class SearchFormServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		Utils.setEncodingToUTF8(response);
		
		Utils.getJSONRPCBridge(request.getSession());
		
		PrintWriter out = response.getWriter();
		HtmlWriter html = new HtmlWriter(out);
		html.start("Search form");
		
		String []dbNames = Voyage.getAllAttrNames();
		
		html.beginScript();
		
		out.println("var fields = [");
		int fieldsCount = 0;
//		for (int i = 0; i < dbNames.length; i++)
//		{
//			SchemaColumn col = null; // Voyage.getSchemaColumn(dbNames[i]);
//			if (fieldsCount > 0) out.println(","); 
//			out.print("{");
//			out.print("name: \"" + col.getName() + "\"");
//			out.print(", ");
//			out.print("label: \"" + col.getName() + "\"");
//			out.print(", ");
//			out.print("type: " + col.getType());
//			if (col.isDictinaory())
//			{
//				out.print(", ");
//				out.print("dictionary: \"" + col.getDictinaory() + "\"");
//			}
//			out.print("}");
//			fieldsCount++;
//		}
//		out.println("];");
		
		out.println("var dictionaries = new Array()");
		HashSet createdDictionaries = new HashSet();
//		for (int i = 0; i < dbNames.length; i++)
//		{
//			//String dbName = dbNames[i];
//			SchemaColumn col = null; //Voyage.getSchemaColumn(dbName);
//			if (col.isDictinaory() && !createdDictionaries.contains(col.getDictinaory()))
//			{
//				createdDictionaries.add(col.getDictinaory());
//				out.println("dictionaries[\"" + col.getDictinaory() + "\"] = [");
//				Dictionary[] dicts = Dictionary.loadDictionary(col.getDictinaory());
//				int dictionaryValues = 0;
//				for (int j = 0; j < dicts.length; j++)
//				{
//					if (dictionaryValues > 0) out.print(",\n"); 
//					out.print("{");
//					out.print("id: " + dicts[j].getId() + ", ");
//					out.print("name: \"" + dicts[j].getName().replaceAll("\"", "\\\\\"") + "\"");
//					out.print("}");
//					dictionaryValues ++;
//				}
//				out.println("];");
//			}
//		}
		
		html.endScript();
		
		html.addExternalScript("search.js");
		
		html.out.print(
				"<form name=\"search\" method=\"get\" action=\"searchresults\" target=\"results\">" +
				"<select name=\"fields\"></select>" +
				"<input type=\"button\" onclick=\"addCondition()\" value=\"Add\">" +
				"<table border=\"1\" id=\"conditionsTable\"></table>" +
				"<input type=\"submit\" value=\"Search\">" +
				"</form>");
		
		html.end();
		
	}

}
