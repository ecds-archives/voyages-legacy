package edu.emory.library.tast.reditor;

public class ListItem
{
	
	private String value;
	private String parentValue;
	private String text;
	
	public ListItem(String value, String parentValue, String text)
	{
		this.value = value;
		this.parentValue = parentValue;
		this.text = text;
	}

	public ListItem(String value, String text)
	{
		this.value = value;
		this.parentValue = null;
		this.text = text;
	}

	public String getParentValue()
	{
		return parentValue;
	}
	
	public void setParentValue(String parentValue)
	{
		this.parentValue = parentValue;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}

}