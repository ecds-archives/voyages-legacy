package edu.emory.library.tast.ui;

public class TimelineMarker
{
	
	private String text;
	private String id;
	
	public TimelineMarker(String text, String id)
	{
		this.text = text;
		this.id = id;
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
}