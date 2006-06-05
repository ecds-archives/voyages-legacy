package edu.emory.library.tas.web.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.Voyage;

public class Detail extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		long voyageId = Utils.getLongParameter(request, "vid");
		List voyages = Voyage.loadAllRevisions(new Long(voyageId), 0);
		String []dbNames = Voyage.getAllAttrNames();

		HtmlWriter html = new HtmlWriter(response.getWriter());
		html.start("Detail");

		html.beginTable(0, 0, 0);
		
		html.beginTr();
		html.td(null);
		for (Iterator iterVoyage = voyages.iterator(); iterVoyage.hasNext();)
		{
			Voyage voyage = (Voyage) iterVoyage.next();
			html.th("Revision " + voyage.getRevisionId());
		}
		html.endTr();
		
		for (int i = 0; i < dbNames.length; i++)
		{
			html.beginTr();
			
			html.th(dbNames[i]);
			
			int j = 0;
			Object prevValue = null;
			Object currValue = null;
			for (Iterator iterVoyage = voyages.iterator(); iterVoyage.hasNext();)
			{
				
				Voyage voyage = (Voyage) iterVoyage.next();
				
				prevValue = currValue;
				currValue = voyage.getAttrValue(dbNames[i]);
				
				String className = j > 0 && (
					(prevValue != null && !prevValue.equals(currValue)) ||
					(prevValue == null && currValue != null))? "new-value" : null;
				
				html.td(currValue, null, className);
				
				j++;
			}
			
			html.endTr();
		}

		html.endTable();
		
		html.end();
	
	}
	
	public static void main(String[] args)
	{
		
		Voyage voyage = Voyage.loadMostRecent(new Long(1));
		voyage.setShipname("Titanic");
		voyage.save();
		
	}

}
