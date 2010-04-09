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
package edu.emory.library.tast.database.graphs;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * Servlet generating PNG image from stored JFreeChart session object.
 * @author Pawel Jurczyk
 *
 */
public class GraphsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		HttpSession session = request.getSession();
		OutputStream stream = response.getOutputStream();
		
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		
		JFreeChart chart = (JFreeChart)session.getAttribute(GraphsBean.SESSION_KEY_GRAPH);
		
		if (chart == null)
		{
			response.encodeRedirectURL("../../images/blank.png");
			return;
		}
		
	    response.setHeader("Cache-Control", "no-cache");
	    response.setHeader("Cache-Control", "max-age=0");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0);		
		
		response.setContentType("image/png");
		ChartUtilities.writeChartAsPNG(stream, chart, width, height);

	}
}
