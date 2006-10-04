package edu.emory.library.tast.ui;

import java.awt.Color;

public class EventLineTestBean2
{
	
	private static final int GRAPH_WIDTH = 800;
	private static final int GRAPH_HEIGHT = 100;
	private static final int ZOOM_LEVEL_MAX = 5;
	private static final int[] ZOOM_LEVEL_SPANS = new int[] {400, 200, 100, 50, 25};
	private static final int[] ZOOM_LEVEL_GAPS = new int[] {100, 50, 25, 10, 5};
	private static final int[] ZOOM_LEVEL_NUDGES = new int[] {25, 25, 25, 10, 5};
	private static final int MIN_YEAR = 1600;
	
	private int firstYear = MIN_YEAR;
	private int zoomLevel = 0;
	private EventLineGraph[] graphs = null;
	private EventLineGraph[] currentGraphs = null;
	
	public EventLineTestBean2()
	{
		createRandomGraphs();
		changeZoomLevelAndPrepareData(0, MIN_YEAR);
	}
	
	private void createRandomGraphs()
	{
		graphs = new EventLineGraph[] {
				createRandomGraph(new Color(0.6f, 0.8f, 1.0f, 0.5f), "Exported"),
				createRandomGraph(new Color(0.6f, 0.8f, 1.0f, 1.0f), "Imported")};
	}
	
	private EventLineGraph createRandomGraph(Color color, String name)
	{

		EventLineDataPoint[] values = new EventLineDataPoint[ZOOM_LEVEL_SPANS[0]];
		int val = (int) (GRAPH_HEIGHT * Math.random());
		values[0] = new EventLineDataPoint(val, String.valueOf(val * 100)); 
		for (int i = 1; i < values.length; i++)
		{
			val = (int) (values[i-1].getValue() + GRAPH_HEIGHT * 0.2 * (Math.random() - 0.5));
			val = Math.min(val, GRAPH_HEIGHT);
			val = Math.max(val, 0);
			values[i] = new EventLineDataPoint(val, String.valueOf(val * 100));
		}
		
		EventLineGraph graph = new EventLineGraph();
		graph.setColor(color);
		graph.setData(values);
		graph.setName(name);
		
		return graph;
		
	}
	
	public int getGraphHeight()
	{
		return GRAPH_HEIGHT;
	}
	
	public int getBarWidth()
	{
		return GRAPH_WIDTH / ZOOM_LEVEL_SPANS[zoomLevel];
	}

	public EventLineGraph[] getCurrentGraphs()
	{
		return currentGraphs;
	}

	private void changeZoomLevelAndPrepareData(int newZoomLevel, int newFirstYear)
	{
		
		zoomLevel = newZoomLevel;
		firstYear = newFirstYear;
		
		if (currentGraphs == null)
			currentGraphs = new EventLineGraph[graphs.length];
		
		for (int i = 0; i < graphs.length; i++)
			currentGraphs[i] = graphs[i].getSubGraph(
					firstYear - MIN_YEAR,
					firstYear - MIN_YEAR + ZOOM_LEVEL_SPANS[zoomLevel] - 1);
		
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

	public EventLineHorizontalLabels getHorizontalLabels()
	{
		
		int gap = ZOOM_LEVEL_GAPS[zoomLevel];
		int gapsCount = ZOOM_LEVEL_SPANS[zoomLevel] / gap;
		
		String[] years = new String[gapsCount + 1];
		for (int i = 0; i <= gapsCount; i++)
			years[i] = String.valueOf(firstYear + i*gap);
		
		EventLineHorizontalLabels labels = new EventLineHorizontalLabels();
		labels.setLabels(years);
		labels.setSpace(ZOOM_LEVEL_SPANS[zoomLevel] / gapsCount);
		
		return labels;

	}

	public EventLineVerticalLabels getVerticalLabels()
	{
		EventLineVerticalLabels labels = new EventLineVerticalLabels();
		labels.setLabels(new String[] {"0", "25", "50", "75", "100"});
		labels.setStart(0);
		labels.setSpace(25);
		return labels;
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

}
