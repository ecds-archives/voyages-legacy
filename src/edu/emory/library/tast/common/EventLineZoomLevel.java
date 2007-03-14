package edu.emory.library.tast.common;

public class EventLineZoomLevel
{
	
	private int barWidth;
	private int labelSpacing;
	private int majorLabels;
	private int viewSpan;
	
	public EventLineZoomLevel(int barWidth, int labelsSpacing, int viewSpan)
	{
		this.barWidth = barWidth;
		this.labelSpacing = labelsSpacing;
		this.viewSpan = viewSpan;
		this.majorLabels = labelsSpacing;
	}

	public EventLineZoomLevel(int barWidth, int labelsSpacing, int viewSpan, int majorSpaces)
	{
		this.barWidth = barWidth;
		this.labelSpacing = labelsSpacing;
		this.viewSpan = viewSpan;
		this.majorLabels = majorSpaces;
	}

	public int getBarWidth()
	{
		return barWidth;
	}
	
	public void setBarWidth(int barWidth)
	{
		this.barWidth = barWidth;
	}
	
	public int getLabelSpacing()
	{
		return labelSpacing;
	}
	
	public void setLabelSpacing(int labelsSpacing)
	{
		this.labelSpacing = labelsSpacing;
	}
	
	public int getViewSpan()
	{
		return viewSpan;
	}
	
	public void setViewSpan(int viewSpan)
	{
		this.viewSpan = viewSpan;
	}

	public int getMajorLabels()
	{
		return majorLabels;
	}

	public void setMajorLabels(int majorSpaces)
	{
		this.majorLabels = majorSpaces;
	}

}
