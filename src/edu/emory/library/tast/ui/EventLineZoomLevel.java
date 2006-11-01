package edu.emory.library.tast.ui;

public class EventLineZoomLevel
{
	
	private int barWidth;
	private int labelsSpacing;
	private int viewSpan;
	
	public EventLineZoomLevel(int barWidth, int labelsSpacing, int viewSpan)
	{
		this.barWidth = barWidth;
		this.labelsSpacing = labelsSpacing;
		this.viewSpan = viewSpan;
	}

	public int getBarWidth()
	{
		return barWidth;
	}
	
	public void setBarWidth(int barWidth)
	{
		this.barWidth = barWidth;
	}
	
	public int getLabelsSpacing()
	{
		return labelsSpacing;
	}
	
	public void setLabelsSpacing(int labelsSpacing)
	{
		this.labelsSpacing = labelsSpacing;
	}
	
	public int getViewSpan()
	{
		return viewSpan;
	}
	
	public void setViewSpan(int viewSpan)
	{
		this.viewSpan = viewSpan;
	}

}
