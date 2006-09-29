package edu.emory.library.tast.ui;

import java.awt.Color;

public class EventLineGraph
{
	
	private EventLineDataPoint[] data;
	private Color color;
	private String name;

	public EventLineDataPoint[] getData()
	{
		if (data == null) data = new EventLineDataPoint[0];
		return data;
	}

	public void setData(EventLineDataPoint[] data)
	{
		this.data = data;
	}

	public int getCount()
	{
		if (data == null) return 0;
		else return data.length;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
