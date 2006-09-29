package edu.emory.library.tast.ui;

public class EventLineEvent
{
	
	private int position;
	private String text;
	
	public EventLineEvent(int position, String text)
	{
		this.position = position;
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
	
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int year)
	{
		this.position = year;
	}

}
