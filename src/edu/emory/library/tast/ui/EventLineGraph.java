package edu.emory.library.tast.ui;


public class EventLineGraph
{
	
	private String name;
	private String baseColor;
	private String eventColor;
	private int x[];
	private double y[];
	
	private boolean maxMinComputed = false;
	private double maxY = 0;
	private double minY = 0;
	
	private void ensureMaxMin()
	{
		if (!maxMinComputed)
		{
			if (y != null)
			{
				maxY = Double.MIN_VALUE;
				minY = Double.MAX_VALUE;
				for (int i = 0; i < y.length; i++)
				{
					if (y[i] > maxY) maxY = y[i];
					if (y[i] < minY) minY = y[i];
				}
			}
			else
			{
				minY = Double.MIN_VALUE;
				maxY = Double.MAX_VALUE;
			}
			maxMinComputed = true;
		}
	}
	
	public double getMaxValue()
	{
		ensureMaxMin();
		return maxY;
	}

	public double getMinValue()
	{
		ensureMaxMin();
		return minY;
	}

	public int[] getX()
	{
		return x;
	}

	public void setX(int[] x)
	{
		this.x = x;
	}

	public double[] getY()
	{
		return y;
	}

	public void setY(double[] y)
	{
		maxMinComputed = false;
		this.y = y;
	}

	public String getBaseColor()
	{
		return baseColor;
	}

	public void setBaseColor(String color)
	{
		this.baseColor = color;
	}

	public String getEventColor()
	{
		return eventColor;
	}

	public String getEventOrBaseColor()
	{
		if (eventColor == null) return baseColor;
		return eventColor;
	}

	public void setEventColor(String eventColor)
	{
		this.eventColor = eventColor;
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