package edu.emory.library.tast.ui;

public class EventLineVerticalLabels
{
	
	private int start;
	private int space;
	private String[] labels;
	
	public int getCount()
	{
		if (labels == null) return 0;
		return labels.length;
	}

	public String getLabel(int i)
	{
		if (labels == null || i < 0 || i >= labels.length) return "";
		return labels[i];
	}
	
	public String[] getLabels()
	{
		return labels;
	}
	
	public void setLabels(String[] labels)
	{
		this.labels = labels;
	}
	
	public int getSpace()
	{
		return space;
	}
	
	public void setSpace(int space)
	{
		this.space = space;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public void setStart(int start)
	{
		this.start = start;
	}

}
