package edu.emory.library.tast.estimates.timeline;

import java.awt.Color;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.ui.EventLineDataPoint;
import edu.emory.library.tast.ui.EventLineEvent;
import edu.emory.library.tast.ui.EventLineGraph;
import edu.emory.library.tast.ui.EventLineHorizontalLabels;
import edu.emory.library.tast.ui.EventLineVerticalLabels;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesTimelineBean
{
	
	private static final int GRAPH_WIDTH = 800;
	private static final int GRAPH_HEIGHT = 100;
	private static final int ZOOM_LEVEL_MAX = 5;
	private static final int[] ZOOM_LEVEL_SPANS = new int[] {400, 200, 100, 50, 25};
	private static final int[] ZOOM_LEVEL_GAPS = new int[] {100, 50, 25, 10, 5};
	private static final int[] ZOOM_LEVEL_NUDGES = new int[] {25, 25, 25, 10, 5};
	private static final int MIN_YEAR = 1500;
	
	private EstimatesSelectionBean selectionBean;
	private Conditions conditions;
	private int barWidth;
	private int zoomLevel = 0;
	private int firstYear = MIN_YEAR;
	private EventLineGraph graphImp;
	private EventLineGraph graphExp;
	private EventLineVerticalLabels verticalLabels;
	private EventLineHorizontalLabels horizontalLabels;
	
	private void generateGraphsIfNecessary()
	{
		
		// conditions from the left column (i.e. from select bean)
		Conditions newConditions = selectionBean.getConditions();

		// check if we have to
		if (newConditions.equals(conditions)) return;
		conditions = newConditions;
		
		// start query
		QueryValue query = new QueryValue(
				new String[] {"Estimate"},
				new String[] {"estimate"},
				conditions);
		
		// group by years
		query.setGroupBy(new Attribute[] {
				Estimate.getAttribute("year")});
		
		// group by years
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new Attribute[] {
				Estimate.getAttribute("year")});

		// list years
		query.addPopulatedAttribute(
				Estimate.getAttribute("year"));

		// sum the number of expoted slaves
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavExported")}));

		// sum the number of imported slaves
		query.addPopulatedAttribute(
				new FunctionAttribute("sum",
						new Attribute[] {Estimate.getAttribute("slavImported")}));

		// finally query the database 
		Object[] result = query.executeQuery();
		
		// nothing?
//		boolean noResults = result == null || result.length == 0; 
//		
//		int minYear = ((Long) ((Object[])result[0])[0]).intValue();
//		int maxYear = ((Long) ((Object[])result[result.length - 1])[0]).intValue();
		
		int yearsSpan = ZOOM_LEVEL_SPANS[zoomLevel]; 
		
		EventLineDataPoint[] impSlaves = new EventLineDataPoint[yearsSpan];
		EventLineDataPoint[] expSlaves = new EventLineDataPoint[yearsSpan];
		
		Object[] row;

		double imp = 0;
		double exp = 0;
		
		double impMax = 0;
		double expMax = 0;
		
		for (int i = 0; i < result.length; i++)
		{
			row = (Object[]) result[i];
			exp = ((Double) row[1]).doubleValue();
			imp = ((Double) row[2]).doubleValue();
			if (i == 0 || expMax < exp) expMax = exp;
			if (i == 0 || impMax < imp) impMax = imp;
		}

		double totalMax = Math.max(impMax, expMax);

		row = (Object[]) result[0];
		int rowYear = ((Integer) row[0]).intValue();

		for (int year = firstYear, resultIndex = 0; year < firstYear + yearsSpan; year++)
		{
			
			System.out.println(rowYear + " : " + year);

			// we have data from db
			if (rowYear == year)
			{
				
				// get values from db
				exp = ((Double) row[1]).doubleValue();
				imp = ((Double) row[2]).doubleValue();
				
				// goto next row
				resultIndex++;
				if (resultIndex < result.length)
				{
					row = (Object[]) result[resultIndex];
					rowYear = ((Integer) row[0]).intValue();
				}
				
			}
			
			// no record for this year
			else
			{
				exp = 0;
				imp = 0;
			}
			
			expSlaves[year - firstYear] = new EventLineDataPoint(
					(int) ((exp / totalMax) * GRAPH_HEIGHT),
					String.valueOf(Math.round(exp * 100) / 100));
			
			impSlaves[year - firstYear] = new EventLineDataPoint(
					(int) ((imp / totalMax) * GRAPH_HEIGHT),
					String.valueOf(Math.round(imp * 100) / 100));

		}
		
		// graph for imported
		graphImp = new EventLineGraph();
		graphImp.setName("Imported");
		graphImp.setData(impSlaves);
		graphImp.setColor(new Color(0.6f, 0.8f, 1.0f, 0.5f));

		// graph for exported
		graphExp = new EventLineGraph();
		graphExp.setName("Exported");
		graphExp.setData(expSlaves);
		graphExp.setColor(new Color(0.6f, 0.8f, 1.0f, 1.0f));

		// bar width
		barWidth = GRAPH_WIDTH / ZOOM_LEVEL_SPANS[zoomLevel];
		
		// generate horizontal labels
		int gap = ZOOM_LEVEL_GAPS[zoomLevel];
		int gapsCount = ZOOM_LEVEL_SPANS[zoomLevel] / gap;
		
		String[] years = new String[gapsCount + 1];
		for (int i = 0; i <= gapsCount; i++)
			years[i] = String.valueOf(firstYear + i*gap);
		
		horizontalLabels = new EventLineHorizontalLabels();
		horizontalLabels.setLabels(years);
		horizontalLabels.setSpace(ZOOM_LEVEL_SPANS[zoomLevel] / gapsCount);

		// generate vertical labels
		verticalLabels = new EventLineVerticalLabels();
		verticalLabels.setLabels(new String[] {"0", "25", "50", "75", "100"});
		verticalLabels.setStart(0);
		verticalLabels.setSpace(25);
		
	}
	
//	public EstimatesTimelineBean()
//	{
//		createRandomGraphs();
//		changeZoomLevelAndPrepareData(0, MIN_YEAR);
//	}
//	
//	private void createRandomGraphs()
//	{
//		graphs = new EventLineGraph[] {
//				createRandomGraph(new Color(0.6f, 0.8f, 1.0f, 0.5f), "Exported"),
//				createRandomGraph(new Color(0.6f, 0.8f, 1.0f, 1.0f), "Imported")};
//	}
//	
//	private EventLineGraph createRandomGraph(Color color, String name)
//	{
//
//		EventLineDataPoint[] values = new EventLineDataPoint[ZOOM_LEVEL_SPANS[0]];
//		int val = (int) (GRAPH_HEIGHT * Math.random());
//		values[0] = new EventLineDataPoint(val, String.valueOf(val * 100)); 
//		for (int i = 1; i < values.length; i++)
//		{
//			val = (int) (values[i-1].getValue() + GRAPH_HEIGHT * 0.2 * (Math.random() - 0.5));
//			val = Math.min(val, GRAPH_HEIGHT);
//			val = Math.max(val, 0);
//			values[i] = new EventLineDataPoint(val, String.valueOf(val * 100));
//		}
//		
//		EventLineGraph graph = new EventLineGraph();
//		graph.setColor(color);
//		graph.setData(values);
//		graph.setName(name);
//		
//		return graph;
//		
//	}
	
	public int getGraphHeight()
	{
		return GRAPH_HEIGHT;
	}
	
	public int getBarWidth()
	{
		generateGraphsIfNecessary();
		return barWidth;
	}

	public EventLineGraph[] getGraphs()
	{
		generateGraphsIfNecessary();
		return new EventLineGraph[] {graphImp, graphExp};
	}

	public EventLineHorizontalLabels getHorizontalLabels()
	{
		generateGraphsIfNecessary();
		return horizontalLabels;
	}

	public EventLineVerticalLabels getVerticalLabels()
	{
		generateGraphsIfNecessary();
		return verticalLabels;
	}
	
	public EventLineEvent[] getEvents()
	{
		return new EventLineEvent[0];
//		return new EventLineEvent[] {
//				new EventLineEvent(2, "<div style=\"border: 1px solid Black; padding: 5px\"><b>To print a webpage</b><br>When a website address cannot be found, Internet Explorer automatically searches the web to try to find that site by using your default search provider. Follow these steps to set your preferences for how this is done.</div>"),
//				new EventLineEvent(15, "<div style=\"border: 1px solid Black; padding: 5px\"><b>Understanding security and privacy features</b><br>Feeds, also known as RSS feeds, XML feeds, syndicated content, or web feeds, contain frequently updated content published by a website. They are usually used for news and blog websites, but are also used for distributing other types of digital content, including pictures, audio files, or video. Internet Explorer can discover and display feeds as you visit websites. You can also subscribe to feeds to automatically check for and download updates that you can view later.</div>"),
//				new EventLineEvent(36, "<div style=\"border: 1px solid Black; padding: 5px\"><b>Delete webpage history</b><br>E-mail is a great way to keep in touch with other people. Unfortunately, it can also open your computer to security risks, computer viruses, and potentially malicious software if you're not careful about the messages and attachments that you open. Before opening any e-mail message or attachment, be sure that you have an up-to-date antivirus program installed. The antivirus program should be configured to scan messages as they arrive (real-time), and to scan all types of file attachments.</div>"),
//				new EventLineEvent(42, "<div style=\"border: 1px solid Black; padding: 5px\"><b>To disable a browser add-on</b><br>If you are about to send information (such as your credit card number) to a site that is not secure, Internet Explorer will warn you. If the site claims to be secure but its security credentials are in question, Internet Explorer will warn you that the site might have been tampered with or might be misrepresenting itself.</div>"),
//				new EventLineEvent(46, "<div style=\"border: 1px solid Black; padding: 5px\"><b>How do browser add-ons affect my computer?</b><br>Add-ons, also known as ActiveX controls, browser extensions, browser helper objects, or toolbars, can improve your experience on a website by providing multimedia or interactive content, such as high-quality animations. However, some add-ons can cause your computer to stop responding or display content that you don't want, such as pop-up ads.</div>"),
//		};
	}
	
	private void changeZoomLevelAndPrepareData(int newZoomLevel, int newFirstYear)
	{
		
//		zoomLevel = newZoomLevel;
//		firstYear = newFirstYear;
//		
//		if (currentGraphs == null)
//			currentGraphs = new EventLineGraph[graphs.length];
//		
//		for (int i = 0; i < graphs.length; i++)
//			currentGraphs[i] = graphs[i].getSubGraph(
//					firstYear - MIN_YEAR,
//					firstYear - MIN_YEAR + ZOOM_LEVEL_SPANS[zoomLevel] - 1);
		
	}
	
	private void zoomTo(int newZoomLevel)
	{
		
		// can we still?
		if (zoomLevel < 0 || ZOOM_LEVEL_MAX <= newZoomLevel)
			return;
		
		// year in the middle of the interval
		int lastYear = firstYear + ZOOM_LEVEL_SPANS[zoomLevel];
		int middleYear = (lastYear + firstYear) / 2;

		// next first point of the interval
		int newSpan = ZOOM_LEVEL_SPANS[newZoomLevel];
		int newFirstYear = middleYear - newSpan / 2;
		
		// adjust to multiples of gaps
		int newGap = ZOOM_LEVEL_GAPS[newZoomLevel];
		newFirstYear = newGap * Math.round(newFirstYear / newGap);
		
		// make sure it's in the max range
		int maxYear = MIN_YEAR + ZOOM_LEVEL_SPANS[0];
		if (newFirstYear < MIN_YEAR)
			newFirstYear = MIN_YEAR;
		else if (maxYear < newFirstYear + newSpan)
			newFirstYear = maxYear - newSpan;
		
		// set it
		changeZoomLevelAndPrepareData(newZoomLevel, newFirstYear);
		
	}
	
	public String zoomMinus()
	{
		zoomTo(zoomLevel - 1);
		return null;
	}

	public String zoomPlus()
	{
		zoomTo(zoomLevel + 1);
		return null;
	}

	public String moveLeft()
	{
		if (firstYear > MIN_YEAR)
		{
			int newFirstYear = firstYear - ZOOM_LEVEL_NUDGES[zoomLevel];
			if (newFirstYear < MIN_YEAR) newFirstYear = MIN_YEAR; 
			changeZoomLevelAndPrepareData(zoomLevel, newFirstYear);
		}
		return null;
	}

	public String moveRight()
	{
		int maxYear = MIN_YEAR + ZOOM_LEVEL_SPANS[0];
		if (firstYear + ZOOM_LEVEL_SPANS[zoomLevel] < maxYear)
		{
			int newFirstYear = firstYear + ZOOM_LEVEL_NUDGES[zoomLevel];
			if (newFirstYear + ZOOM_LEVEL_SPANS[zoomLevel] > maxYear) newFirstYear = maxYear - ZOOM_LEVEL_SPANS[zoomLevel]; 
			changeZoomLevelAndPrepareData(zoomLevel, newFirstYear);
		}
		return null;
	}

	
	public String getMoveLeftLabel()
	{
		return "< (previous " + ZOOM_LEVEL_NUDGES[zoomLevel] + " years)";
	}

	public String getMoveRightLabel()
	{
		return "(next " + ZOOM_LEVEL_NUDGES[zoomLevel] + " years) >";
	}

	public String getMoveZoomPlusLabel()
	{
		if (zoomLevel < ZOOM_LEVEL_MAX - 1)
			return "+ (zoom to " + ZOOM_LEVEL_SPANS[zoomLevel + 1] + " scale)";
		else
			return "";
	}

	public String getMoveZoomMinusLabel()
	{
		if (zoomLevel > 0)
			return "- (zoom to " + ZOOM_LEVEL_SPANS[zoomLevel - 1] + " scale)";
		else
			return "";
	}

	public EstimatesSelectionBean getSelectionBean()
	{
		return selectionBean;
	}

	public void setSelectionBean(EstimatesSelectionBean selectionBean)
	{
		this.selectionBean = selectionBean;
	}

}
