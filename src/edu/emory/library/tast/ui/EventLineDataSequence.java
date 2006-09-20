package edu.emory.library.tast.ui;

public class EventLineDataSequence
{
	
	private int[] data;
	private String color;

	public int[] getData()
	{
		if (data == null) data = new int[0];
		return data;
	}

	public void setData(int[] data)
	{
		this.data = data;
	}
	
	public int getCount()
	{
		if (data == null) return 0;
		else return data.length;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

}
