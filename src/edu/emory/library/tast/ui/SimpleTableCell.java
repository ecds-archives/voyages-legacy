package edu.emory.library.tast.ui;

public class SimpleTableCell
{

	private String text;
	private String cssStyle;
	private String cssClass;
	private int rowspan = 1;
	private int colspan = 1;
	
	public SimpleTableCell(String text)
	{
		this.text = text;
	}
	
	public SimpleTableCell(String text, String cssClass)
	{
		this.text = text;
		this.cssStyle = cssClass;
	}
	
	public SimpleTableCell(String text, String cssClass, String cssStyle)
	{
		this.text = text;
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
	}

	public SimpleTableCell(String text, String cssStyle, String cssClass, int rowspan, int colspan)
	{
		this.text = text;
		this.cssStyle = cssStyle;
		this.cssClass = cssClass;
		this.rowspan = rowspan;
		this.colspan = colspan;
	}

	public SimpleTableCell(String text, int rowspan, int colspan)
	{
		this.text = text;
		this.rowspan = rowspan;
		this.colspan = colspan;
	}

	public boolean hasCssStyle()
	{
		return cssStyle != null && cssStyle.length() > 0;
	}

	public boolean hasCssClass()
	{
		return cssClass != null && cssClass.length() > 0;
	}

	public int getColspan()
	{
		return colspan;
	}
	
	public SimpleTableCell setColspan(int colspan)
	{
		this.colspan = colspan;
		return this;
	}
	
	public String getCssStyle()
	{
		return cssStyle;
	}
	
	public SimpleTableCell setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
		return this;
	}

	public int getRowspan()
	{
		return rowspan;
	}
	
	public SimpleTableCell setRowspan(int rowspan)
	{
		this.rowspan = rowspan;
		return this;
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

	public SimpleTableCell setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
		return this;
	}
	
}
