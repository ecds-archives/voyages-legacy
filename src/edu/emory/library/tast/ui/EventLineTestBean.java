package edu.emory.library.tast.ui;

import java.awt.Color;

public class EventLineTestBean
{
	
	private static final int VALUES = 600;
	private static final int GRAPH_HEIGHT = 100;
	private static final int START_YEAR = 1600;
	private static final int YEAR_SPACE = 50;

	public EventLineEvent[] getEvents()
	{
		return null;
	}
	
	public int getGraphHeight()
	{
		return GRAPH_HEIGHT;
	}
	
	private EventLineGraph createRandomGraph(Color color, String name)
	{

		int[] values = new int[VALUES];
		values[0] = (int) (GRAPH_HEIGHT * Math.random());
		for (int i = 1; i < values.length; i++)
		{
			int val = (int) (values[i-1] + GRAPH_HEIGHT * 0.2 * (Math.random() - 0.5));
			val = Math.min(val, GRAPH_HEIGHT);
			val = Math.max(val, 0);
			values[i] = val;
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
