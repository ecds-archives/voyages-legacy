package edu.emory.library.tast.common;

public class EventLineEvent
{
	
	private int x;
	private String text;
	
	public EventLineEvent(int x, String text)
	{
		this.x = x;
		this.text = text;
	}

	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int year)
	{
		this.x = year;
	}

}
