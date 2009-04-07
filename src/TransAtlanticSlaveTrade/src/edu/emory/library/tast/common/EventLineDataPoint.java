package edu.emory.library.tast.common;

import edu.emory.library.tast.util.StringUtils;

public class EventLineDataPoint
{
	
	private int value;
	private String label = null;
	
	public EventLineDataPoint(int value)
	{
		this.value = value;
		this.label = String.valueOf(value);
	}
	
	public EventLineDataPoint(int value, String label)
	{
		this.value = value;
		this.label = label;
	}

	public boolean hasLabel()
	{
		return StringUtils.isNullOrEmpty(label);
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}

}
