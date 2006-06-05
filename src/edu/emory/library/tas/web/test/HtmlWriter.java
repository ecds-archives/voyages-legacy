package edu.emory.library.tas.web.test;

import java.io.PrintWriter;

public class HtmlWriter
{
	
	public PrintWriter out;
	
	public HtmlWriter(PrintWriter out)
	{
		this.out = out;
	}
	
	public void start(String title)
	{
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + title + "</title>");
		out.println("<link href=\"common.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("</head>");
	}

	public void end()
	{
		out.println("</html>");
	}
	
	private void beginElement(String element, String[] attNames, String[] attValues)
	{
		out.print("<" + element);
		if (attNames != null && attValues !=null && attNames.length == attValues.length)
		{
			for (int i=0; i<attNames.length; i++)
			{
				if (attNames[i] != null && attValues[i] != null)
				{
					out.print(" ");
					out.print(attNames[i]);
					out.print("=\"");
					out.print(attValues[i]);
					out.print("\"");
				}
			}
		}
		out.print(">");
	}

	private void endElement(String element)
	{
		out.print("</" + element + ">");
	}

	private void element(String element, Object text, String[] attNames, String[] attValues)
	{
		beginElement(element, attNames, attValues);
		if (text != null) out.print(text);
		endElement(element);
	}

	public void beginTable(int border, int cellSpacing, int cellPadding)
	{
		beginTable(border, cellSpacing, cellPadding, null, null);
	}

	public void beginTable(int border, int cellSpacing, int cellPadding, String style, String className)
	{
		beginElement("table",
				new String[] {"border", "cellspacing", "cellpadding", "style", "class"},
				new String[] {String.valueOf(border), String.valueOf(cellSpacing), String.valueOf(cellPadding), style, className});
		out.println();
	}

	public void endTable()
	{
		endElement("table");
		out.println();
	}

	public void beginTr()
	{
		beginElement("tr", null, null);
		out.println();
	}

	public void endTr()
	{
		endElement("tr");
		out.println();
	}

	public void th(Object text)
	{
		element("th", text, null, null);
		out.println();
	}

	public void th(Object text, String style, String className)
	{
		element("th", text, new String[] {"style", "class"}, new String[] {style, className});
		out.println();
	}

	public void tdWithHref(Object text, String href)
	{
		beginElement("td", null, null);
		a(text, href);
		endElement("td");
	}

	public void td(Object text)
	{
		element("td", text, null, null);
		out.println();
	}

	public void td(Object text, String style, String className)
	{
		element("td", text, new String[] {"style", "class"}, new String[] {style, className});
		out.println();
	}

	public void a(Object text, String href)
	{
		element("a", text, new String[] {"href"}, new String[] {href});
	}

	public void a(Object text, String href, String style, String className)
	{
		element("th", text, new String[] {"href", "style", "class"}, new String[] {href, style, className});
	}

}
