package edu.emory.library.tas.web.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.QueryValue;

public class StatisticalSheet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
//		HttpSession session = request.getSession();
		
		System.out.println("Do get");
		
		String[] attrs = Voyage.getAllAttrNames();
		
		boolean statChecked = request.getParameter("statEnabled") != null;
		
//		Enumeration enum = request.getParameterNames();
//		while (enum.hasMoreElements()) {
//			System.out.println("  " + enum.nextElement());
//		}
		
		Object type = request.getParameter("buttonType");;
		Object stat = request.getParameter("statF");
		Object attr = request.getParameter("attrName");
		Object group = request.getParameter("attrGroup");
		Object ser = request.getParameter("chartSeries");
		
//		JFreeChart chart = null;
		
		String[] series = new String[] {};
		String nSeriesLong = "";
		if (ser != null) { 			
			series = ((String)ser).split(";");
		}
		
		if ("add".equals(type)) {
			String[] nSeries = new String[series.length];
			String newAttr = null;
			for (int i = 0; i < series.length; i++) {
				nSeries[i] = series[i];
			}
			if (stat != null) {
				newAttr = stat + "(" + (String)attr + ")";
			} else {
				newAttr = (String)attr;
			}
			nSeries[nSeries.length - 1] = newAttr;
			series = nSeries;
			for (int i = 0; i < nSeries.length; i++) {
				nSeriesLong += nSeries[i];
				nSeriesLong += ";";				
			}
		} else if (type != null && ((String)type).startsWith("remove_")) {
			String toRem = ((String)type).substring(7, ((String)type).length());
			String[] nSeries = new String[series.length - 2];
			int j = 0;
			for (int i = 0; i < series.length - 1; i++) {
				if (!series[i].equals(toRem)) {
					nSeries[j] = series[i];
					nSeriesLong += series[i];
					nSeriesLong += ";";
					j++;
				}
			}
			series = nSeries;
		} else if ("generate".equals(type)) {
			String[] nSeries = new String[series.length - 1];
			
			QueryValue qValue = new QueryValue("VoyageIndex");
			qValue.setLimit(40);
			qValue.setConditions(VoyageIndex.getRecent());
			for (int i = 0; i < series.length - 1; i++) {
				nSeries[i] = series[i];
				if (!statChecked) {
					qValue.addPopulatedAttribute("voyage." + series[i], false);
				} else {
					String function = series[i].substring(0, series[i].indexOf("("));
					String arg = series[i].substring(series[i].indexOf("(") + 1, series[i].indexOf(")"));
					qValue.addPopulatedAttribute(function + "(voyage." + arg + ")", false);
				}
			}
			for (int i = 0; i < series.length - 1; i++) {
				nSeriesLong += series[i];
				nSeriesLong += ";";				
			}
			series = nSeries;
			
			if (!statChecked) {
				qValue.addPopulatedAttribute("voyage.voyageId", false);
			} else {
				qValue.addPopulatedAttribute("voyage." + (String)group, false);
				qValue.setGroupBy(new String[] {"voyage." + (String)group});
			}
			
			//Object [] objs = HibernateConnector.getConnector().loadObjects(qValue);
			
//			DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
//			
//			for (int i = 0; i < objs.length; i++) {
//				
//				for (int j = 0; j < series.length; j++) {					
//					float n = 0.0F;
//					if (((Object[])objs[i])[j] != null) {
//						n = ((Number)((Object[])objs[i])[j]).floatValue();
//					}
//					
//					categoryDataset.addValue(n, ((Object[])objs[i])[series.length].toString(), series[j]);
//				}
//			
//			}
			
//			chart = ChartFactory.createBarChart("Sample Category Chart", // Title
//			                      "Voyages",              // X-Axis label
//			                      "# of slaves",                 // Y-Axis label
//			                      categoryDataset,         // Dataset
//			                      PlotOrientation.VERTICAL,
//			                      true, true, true                     // Show legend
//			                     );

			
			
		} else if (series.length > 0) {
			String[] nSeries = new String[series.length - 1];
			
			QueryValue qValue = new QueryValue("VoyageIndex");
			qValue.setLimit(40);
			
			for (int i = 0; i < series.length - 1; i++) {
				nSeries[i] = series[i];
				qValue.addPopulatedAttribute("voyage." + series[i], false);
			}
			for (int i = 0; i < series.length - 1; i++) {
				nSeriesLong += series[i];
				nSeriesLong += ";";				
			}
			series = nSeries;
		}

	
		
		
		
		HtmlWriter html = new HtmlWriter(response.getWriter());
		html.start("Statistical sheet");
		html.out.println("<body onload=\"document.forms[0].statF.value='" + stat + "';" +
				"document.forms[0].attrName.value='" + attr + "';" +
				"document.forms[0].attrGroup.value='" + group + "';\">");
		html.out.println("<form>");
		html.beginTable(1, 1, 1, "stats.css", "mainTable");
		html.th("Statistics preparation");		
		html.beginTr();
	
		html.out.println("<td>");
			html.beginTable(1, 1, 1, "stats.css", "mainTable");
			html.th("Y axis value");
			html.th("Grouping");
			html.beginTr();
			//document.getElementById('statF').value=3
			html.out.println("<input type=\"hidden\" name=\"chartSeries\" value=\""+ nSeriesLong +"1\">");
			html.out.println("<input type=\"hidden\" name=\"buttonType\" value=\"\">");
			html.out.println("<td>");
			html.out.println("<input type=\"checkbox\" name=\"statEnabled\" selected=\"true\" value=\"true\" " + (statChecked?"checked":"") + " onClick=\"document.forms[0].submit();\"> Statistical functions<br>");
			html.out.println("<select name=\"statF\" " + (statChecked ? "" : " disabled") + ">");
			html.out.println("<option value=\"avg\">Average</option>");
			html.out.println("<option value=\"max\">Max</option>");
			html.out.println("<option value=\"min\">Min</option>");
			html.out.println("<option value=\"sum\">Sum</option>");
			html.out.println("</select>");
			html.out.println("<select name=\"attrName\">");
			for (int i = 0; i < attrs.length; i++) {
				html.out.print("<option value=\"" + attrs[i] + "\">" + attrs[i] + "</option>");
			}
			html.out.println("</select>");
			html.out.println("<input type=\"submit\" value=\"Add\" onclick=\"document.forms[0].buttonType.value='add'; document.forms[0].submit();\">");
			html.out.println("</td>");
			
			html.out.println("<td>");
			html.out.println("Group by:<br/>");
			html.out.println("<select name=\"attrGroup\""+ (statChecked ? "" : " disabled") + ">");
			for (int i = 0; i < attrs.length; i++) {
				html.out.print("<option value=\"" + attrs[i] + "\">" + attrs[i] + "</option>");
			}
			html.out.println("</select>");
			html.out.println("</td>");
			
			html.endTr();
			html.endTable();
		
			html.beginTable(1, 1, 1, "stats.css", "mainTable");
			html.th("Series");
			html.th("");
			html.beginTr();
			
			for (int i = 0; i < series.length; i++) {
				html.out.println("<tr>");
				html.out.println("<td>");				
				html.out.println(series[i]);				
				html.out.println("</td>");
				html.out.println("<td>");
				html.out.println("<input type=\"submit\" value=\"Remove\" onclick=\"document.forms[0].buttonType.value='remove_" + series[i] + "'; document.forms[0].submit();\">");
				html.out.println("</td>");
				html.out.println("</tr>");
			}
			
			html.endTr();
			html.endTable();
			
		html.out.println("<br/>");
		html.out.println("<input type=\"submit\" value=\"Generate statistic\" onclick=\"document.forms[0].buttonType.value='generate'; document.forms[0].submit();\">");
		html.out.println("</td>");
		
		
		html.endTr();		
		html.endTable();
//		if (chart != null) {
//			ServletContext context = session.getServletContext();
//			String realContextPath = context.getRealPath(request.getContextPath());
//			realContextPath = realContextPath.substring(0, realContextPath.lastIndexOf("/"));
//			System.out.println(realContextPath);
//			
//			ChartUtilities.saveChartAsPNG(new File(realContextPath + "/chart.png"), chart, 800, 600);
//			html.out.println("<img src=\"chart.png\" alt=\"Chart\">");
//		}
		html.out.println("</form>");
		html.out.println("</body>");
		html.end();
		
	}

	protected void doPut(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(arg0, arg1);
		System.out.println("Do put!");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
}
	
}
