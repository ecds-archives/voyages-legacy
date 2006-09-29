package edu.emory.library.tast.ui;

import java.awt.Color;

public class EventLineTestBean
{
	
	private static final int VALUES = 50;
	private static final int GRAPH_HEIGHT = 100;
	private static final int START_YEAR = 1600;
	private static final int YEAR_SPACE = 10;

	public EventLineEvent[] getEvents()
	{
		return new EventLineEvent[] {
				new EventLineEvent(2, "<div style=\"border: 1px solid Black; padding: 5px\"><b>To print a webpage</b><br>When a website address cannot be found, Internet Explorer automatically searches the web to try to find that site by using your default search provider. Follow these steps to set your preferences for how this is done.</div>"),
				new EventLineEvent(15, "<div style=\"border: 1px solid Black; padding: 5px\"><b>Understanding security and privacy features</b><br>Feeds, also known as RSS feeds, XML feeds, syndicated content, or web feeds, contain frequently updated content published by a website. They are usually used for news and blog websites, but are also used for distributing other types of digital content, including pictures, audio files, or video. Internet Explorer can discover and display feeds as you visit websites. You can also subscribe to feeds to automatically check for and download updates that you can view later.</div>"),
				new EventLineEvent(36, "<div style=\"border: 1px solid Black; padding: 5px\"><b>Delete webpage history</b><br>E-mail is a great way to keep in touch with other people. Unfortunately, it can also open your computer to security risks, computer viruses, and potentially malicious software if you're not careful about the messages and attachments that you open. Before opening any e-mail message or attachment, be sure that you have an up-to-date antivirus program installed. The antivirus program should be configured to scan messages as they arrive (real-time), and to scan all types of file attachments.</div>"),
				new EventLineEvent(42, "<div style=\"border: 1px solid Black; padding: 5px\"><b>To disable a browser add-on</b><br>If you are about to send information (such as your credit card number) to a site that is not secure, Internet Explorer will warn you. If the site claims to be secure but its security credentials are in question, Internet Explorer will warn you that the site might have been tampered with or might be misrepresenting itself.</div>"),
				new EventLineEvent(46, "<div style=\"border: 1px solid Black; padding: 5px\"><b>How do browser add-ons affect my computer?</b><br>Add-ons, also known as ActiveX controls, browser extensions, browser helper objects, or toolbars, can improve your experience on a website by providing multimedia or interactive content, such as high-quality animations. However, some add-ons can cause your computer to stop responding or display content that you don't want, such as pop-up ads.</div>"),
		};
	}
	
	public int getGraphHeight()
	{
		return GRAPH_HEIGHT;
	}
	
	public int getBarWidth()
	{
		return 800 / VALUES;
	}
	
	private EventLineGraph createRandomGraph(Color color, String name)
	{

		EventLineDataPoint[] values = new EventLineDataPoint[VALUES];
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
		graph.setName("Imported");
		
		return graph;
		
	}

	public EventLineGraph[] getGraphs()
	{
		
		
//		EventLineGraph graph2 = new EventLineGraph();
//		graph2.setColor(new Color(0.0f, 1.0f, 0.0f, 0.5f));
//		graph2.setData(new int[] {5, 10, 60, 30, 50, 0});

		return new EventLineGraph[] {
				createRandomGraph(new Color(0.6f, 0.8f, 1.0f, 0.5f), "Exported"),
				createRandomGraph(new Color(0.6f, 0.8f, 1.0f, 1.0f), "Imported")
		};

	}
	
	public EventLineHorizontalLabels getHorizontalLabels()
	{
		
		String[] years = new String[VALUES / YEAR_SPACE + 1];
		for (int i = 0; i < years.length; i++)
			years[i] = String.valueOf(START_YEAR + i*YEAR_SPACE);
		
		EventLineHorizontalLabels labels = new EventLineHorizontalLabels();
		labels.setLabels(years);
		labels.setSpace(YEAR_SPACE);
		
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

}
