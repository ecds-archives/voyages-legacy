/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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