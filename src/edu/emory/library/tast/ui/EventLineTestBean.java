package edu.emory.library.tast.ui;

import java.awt.Color;

public class EventLineTestBean
{
	
	public EventLineEvent[] getEvents()
	{
		return null;
	}

	public EventLineGraph[] getGraphs()
	{
		
		EventLineGraph graph1 = new EventLineGraph();
		graph1.setColor(new Color(0.0f, 1.0f, 1.0f, 1.0f));
		graph1.setData(new int[] {0, 20, 10, 50, 0, 60});
		
		EventLineGraph graph2 = new EventLineGraph();
		graph2.setColor(new Color(1.0f, 1.0f, 0.0f, 1.0f));
		graph2.setData(new int[] {5, 10, 60, 30, 50, 0});

		return new EventLineGraph[] {graph1, graph2};

	}

}
