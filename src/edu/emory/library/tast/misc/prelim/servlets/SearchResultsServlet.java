package edu.emory.library.tast.misc.prelim.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.dm.attributes.AbstractAttribute;
import edu.emory.library.tast.util.HibernateConnector;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SearchResultsServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		Utils.setEncodingToUTF8(response);
		
		PrintWriter out = response.getWriter();
		HtmlWriter html = new HtmlWriter(out);
		html.start("Search results");
		
		String[] selectedConditions = request.getParameterValues("selected-conditions");
		
		Conditions conditions = new Conditions(Conditions.JOIN_AND);
		
		for (int i = 0; i<selectedConditions.length; i++)
		{
			edu.emory.library.tast.dm.attributes.Attribute attr = Voyage.getAttribute(selectedConditions[i]);
			
			switch (attr.getType().intValue())
			{
				case AbstractAttribute.TYPE_STRING:
					
					String valueStr = request.getParameter(attr.getName()); 
					conditions.addCondition(
							"voyage." + attr.getName(),
							valueStr + "%",
							Conditions.OP_LIKE);
					
					out.println(
							"Field " +
							"<b>" + attr.getName() + "</b>" +
							" = " +
							valueStr + "<br>");
					break;
				
				case AbstractAttribute.TYPE_INTEGER:
				case AbstractAttribute.TYPE_FLOAT:
				case AbstractAttribute.TYPE_LONG:
				case AbstractAttribute.TYPE_DATE:
					String rangeType = request.getParameter(attr.getName() + "-type");
					String rangeRs = request.getParameter(attr.getName() + "-rs");
					String rangeRe = request.getParameter(attr.getName() + "-re");
					String rangeLe = request.getParameter(attr.getName() + "-le");
					String rangeGe = request.getParameter(attr.getName() + "-ge");
					String rangeEq = request.getParameter(attr.getName() + "-eq");
					if ("range".equals(rangeType) && rangeRs != null && rangeRe != null)
					{
						out.println(
								"Field " +
								"<b>" + attr.getName() + "</b>" +
								" is between " +
								rangeRs + " and " + rangeRe + "<br>");
					}
					else if ("le".equals(rangeType) && rangeLe != null)
					{
						out.println(
								"Field " +
								"<b>" + attr.getName() + "</b>" + 
								" <= " + rangeLe + "<br>");
					}
					else if ("ge".equals(rangeType) && rangeGe != null)
					{
						out.println(
								"Field " +
								"<b>" + attr.getName() + "</b>" +
								" >= " + rangeGe + "<br>");
					}
					else if ("eq".equals(rangeType) && rangeEq != null)
					{
						out.println(
								"Field " +
								"<b>" + attr.getName() + "</b>" +
								" = " + rangeEq + "<br>");
					}
					break;

				case AbstractAttribute.TYPE_DICT:

					String valueDict = request.getParameter(attr.getName()); 
					conditions.addCondition(
							"voyage." + attr.getName() + ".id",
							new Long(valueDict),
							Conditions.OP_EQUALS);
					
					out.println(
							"Dictionary " +
							"<b>" + attr.getName() + "</b>" +
							" = " +
							valueDict + "<br>");
					break;

			}
			
		}
		
		QueryValue queryValue = new QueryValue("VoyageIndex as voyageindex inner join fetch voyageindex.voyage as voyage", conditions, 100);
		conditions.addCondition(VoyageIndex.getRecent());
		
		long t1 = System.currentTimeMillis();
		Object[] voyages = HibernateConnector.getConnector().loadObjects(queryValue);
		long t2 = System.currentTimeMillis();
		out.println("time = " + (t2 - t1));
		
		html.beginTable(0, 0, 0, "width: 100%", "grid");

		html.beginTr();
		html.th("");
		html.th("ID");
		html.th("Ship");
		html.th("Captain");
		html.th("Started");
		html.th("Ended");
		html.th("Embarked");
		html.th("Disembarked");
		html.th("From");
		html.th("To");
		html.endTr();
		
		for (int i = 0; i < voyages.length; i++)
		{
			Voyage voyage = ((VoyageIndex)voyages[i]).getVoyage();
			
			html.beginTr();
			html.td((i+1) + ".");
			html.tdWithHref(voyage.getVoyageId(), "detail?vid=" + voyage.getVoyageId());
			
			html.td(voyage.getShipname());
			html.td(voyage.getCaptaina());

			html.td(voyage.getYearaf());
			html.td(voyage.getYearam());
			
			html.td(voyage.getSlaximp());
			html.td(voyage.getSlamimp());
			
			if (voyage.getMajbuypt() != null) html.td(voyage.getMajbuypt().getName());
			if (voyage.getMajselpt() != null) html.td(voyage.getMajselpt().getName());
			
			html.endTr();
			
		}

		html.endTable();
		
		html.end();
		
	}

}
