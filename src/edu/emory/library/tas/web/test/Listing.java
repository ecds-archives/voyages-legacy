package edu.emory.library.tas.web.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.Voyage;

public class Listing extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	private static final int PAGE_SIZE = 20;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		Utils.setEncodingToUTF8(response);
		
		int firstRecord = Utils.getIntParameter(request, "first-record");
		Voyage[] voyages = Voyage.loadAllMostRecent(firstRecord, PAGE_SIZE);

		HtmlWriter html = new HtmlWriter(response.getWriter());
		html.start("List");

		html.beginTable(0, 0, 0);
		html.beginTr();
		
		if (firstRecord > 0)
			html.tdWithHref(
					"Previous " + PAGE_SIZE,
					"listing?first-record=" + (firstRecord - PAGE_SIZE));
		
		html.tdWithHref(
				"Next " + PAGE_SIZE,
				"listing?first-record=" + (firstRecord + PAGE_SIZE));
		
		html.endTr();
		html.endTable();
		
		html.out.println("<br>");

		html.beginTable(0, 0, 0, "width: 100%", null);

		html.beginTr();
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
			Voyage voyage = voyages[i];
			
			html.beginTr();
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