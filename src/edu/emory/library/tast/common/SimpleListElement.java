package edu.emory.library.tast.common;

public class SimpleListElement
{
	
	private String text;
	private String cssClass;
	private String cssStyle;
	
	public SimpleListElement(String text)
	{
		this.text = text;
	}

	public SimpleListElement(String text, String cssClass)
	{
		this.text = text;
		this.cssClass = cssClass;
	}

	public SimpleListElement(String text, String cssClass, String cssStyle)
	{
		this.text = text;
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
	}

	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getCssClass()
	{
		return cssClass;
	}
	
	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public String getCssStyle()
	{
		return cssStyle;
	}

	public void setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
	}

}
