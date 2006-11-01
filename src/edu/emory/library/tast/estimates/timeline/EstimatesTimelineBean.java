package edu.emory.library.tast.estimates.timeline;

import edu.emory.library.tast.dm.Estimate;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.estimates.selection.EstimatesSelectionBean;
import edu.emory.library.tast.ui.EventLineEvent;
import edu.emory.library.tast.ui.EventLineGraph;
import edu.emory.library.tast.ui.EventLineVerticalLabels;
import edu.emory.library.tast.ui.EventLineZoomLevel;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class EstimatesTimelineBean
{
	
	private EstimatesSelectionBean selectionBean;
	private Conditions conditions;
	private EventLineGraph graphImp;
	private EventLineGraph graphExp;
	private EventLineVerticalLabels verticalLabels;
	
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

		// data arrays
		int expYears[] = new int[result.length];
		int impYears[] = new int[result.length];
		double expValues[] = new double[result.length];
		double impValues[] = new double[result.length];
		
		// move db data to it
		for (int i = 0; i < result.length; i++)
		{
			Object[] row = (Object[]) result[i];
			impYears[i] = expYears[i] = ((Integer) row[0]).intValue();
			expValues[i] = ((Double) row[1]).doubleValue();
			impValues[i] = ((Double) row[2]).doubleValue();
		}
		
		// graph for exported
		graphExp = new EventLineGraph();
		graphExp.setName("Exported");
		graphExp.setX(expYears);
		graphExp.setY(expValues);
		graphExp.setColor("#EEEEEE");

		// graph for imported
		graphImp = new EventLineGraph();
		graphImp.setName("Imported");
		graphImp.setX(impYears);
		graphImp.setY(impValues);
		graphImp.setColor("#CCCCCC");

		// generate vertical labels
		verticalLabels = new EventLineVerticalLabels();
		verticalLabels.setLabels(new String[] {"0", "25", "50", "75", "100"});
		verticalLabels.setStart(0);
		verticalLabels.setSpace(25);
		
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
	
	public EventLineZoomLevel[] getZoomLevels()
	{
		return new EventLineZoomLevel[] {
				new EventLineZoomLevel(2, 100, 400),
				new EventLineZoomLevel(4, 50, 200),
				new EventLineZoomLevel(8, 25, 100),
				new EventLineZoomLevel(16, 10, 50),
				new EventLineZoomLevel(32, 5, 25)};
	}

	public EventLineGraph[] getGraphs()
	{
		generateGraphsIfNecessary();
		return new EventLineGraph[] {graphExp, graphImp};
	}

	public EventLineVerticalLabels getVerticalLabels()
	{
		generateGraphsIfNecessary();
		return verticalLabels;
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
