package edu.emory.library.tast.common;

public class EventLineHorizontalLabels
{
	
	private String[] labels;
	private int space = 1;
	private int start = 0;
	
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
