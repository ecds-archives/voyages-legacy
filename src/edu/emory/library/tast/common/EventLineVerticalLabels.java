package edu.emory.library.tast.common;

public class EventLineVerticalLabels
{
	
	private double majorSpacing;
	private double spacing;
	
	public EventLineVerticalLabels(double majorSpacing, double spacing)
	{
		this.majorSpacing = majorSpacing;
		this.spacing = spacing;
	}
	
	public EventLineVerticalLabels(int spacing)
	{
		this.spacing = spacing;
		this.majorSpacing = spacing; 
	}

	public double getMajorSpacing()
	{
		return majorSpacing;
	}
	
	public void setMajorSpacing(int majorSpacing)
	{
		this.majorSpacing = majorSpacing;
	}
	
	public double getSpacing()
	{
		return spacing;
	}

	public void setSpacing(int spacing)
	{
		this.spacing = spacing;
	}

}
