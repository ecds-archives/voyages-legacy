package edu.emory.library.tast.ui;

public class EventLineVerticalLabels
{
	
	private int majorSpacing;
	private int spacing;
	
	public EventLineVerticalLabels(int majorSpacing, int spacing)
	{
		this.majorSpacing = majorSpacing;
		this.spacing = spacing;
	}
	
	public EventLineVerticalLabels(int spacing)
	{
		this.spacing = spacing;
		this.majorSpacing = spacing; 
	}

	public int getMajorSpacing()
	{
		return majorSpacing;
	}
	
	public void setMajorSpacing(int majorSpacing)
	{
		this.majorSpacing = majorSpacing;
	}
	
	public int getSpacing()
	{
		return spacing;
	}

	public void setSpacing(int spacing)
	{
		this.spacing = spacing;
	}

}
