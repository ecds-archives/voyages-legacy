package edu.emory.library.tast.ui;

public class EventLineEvent
{
	
	private int position;
	private String title;
	private String text;
	
	public EventLineEvent(int year, String title, String text)
	{
		this.position = year;
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
