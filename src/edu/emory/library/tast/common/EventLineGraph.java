package edu.emory.library.tast.common;


public class EventLineGraph
{
	
	private String name;
	private String baseCssClass;
	private String eventCssClass;
	private int[] x;
	private double[] y;
	private String[] labels;
	
	private boolean maxMinComputed = false;
	private double maxY = 0;
	private double minY = 0;
	
	private double explicitMax;
	private double explicitMin;
	private boolean explicitMaxSet = false;
	private boolean explicitMinSet = false;
	
	private void ensureMaxMin()
	{
		if (!maxMinComputed)
		{
			if (explicitMaxSet && explicitMinSet)
			{
				minY = explicitMin;
				maxY = explicitMax;
			}
			else
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
				if (explicitMaxSet) maxY = explicitMax;
				if (explicitMinSet) maxY = explicitMin;
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

	public String getBaseCssClass()
	{
		return baseCssClass;
	}

	public void setBaseCssClass(String color)
	{
		this.baseCssClass = color;
	}

	public String getEventCssClass()
	{
		return eventCssClass;
	}

	public String getEventOrBaseColor()
	{
		if (eventCssClass == null) return baseCssClass;
		return eventCssClass;
	}

	public void setEventCssClass(String eventColor)
	{
		this.eventCssClass = eventColor;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String[] getLabels()
	{
		return labels;
	}

	public void setLabels(String[] labels)
	{
		this.labels = labels;
	}

	public void unsetExplicitMax()
	{
		this.explicitMaxSet = false;
		maxMinComputed = false;
	}

	public void unsetExplicitMin()
	{
		this.explicitMaxSet = false;
		maxMinComputed = false;
	}

	public void setExplicitMax(double explicitMax)
	{
		this.explicitMaxSet = true;
		this.explicitMax = explicitMax;
		maxMinComputed = false;
	}

	public void setExplicitMin(double explicitMin)
	{
		this.explicitMinSet = true;
		this.explicitMin = explicitMin;
		maxMinComputed = false;
	}

	public boolean isExplicitMaxSet()
	{
		return explicitMaxSet;
	}

	public boolean isExplicitMinSet()
	{
		return explicitMinSet;
	}
	
}