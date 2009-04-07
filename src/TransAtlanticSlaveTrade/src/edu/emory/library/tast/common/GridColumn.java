package edu.emory.library.tast.common;

import edu.emory.library.tast.util.StringUtils;

public class GridColumn
{
	
	private String name;
	private String header;
	private String cssClass;
	private boolean clickable = false;
	
	public GridColumn(String header)
	{
		this.header = header;
	}
	
	public GridColumn(String name, String header)
	{
		this.name = name;
		this.header = header;
	}

	public GridColumn(String name, String header, String cssClass)
	{
		this.name = name;
		this.header = header;
		this.cssClass = cssClass;
	}

	public GridColumn(String name, String header, boolean clickable)
	{
		this.name = name;
		this.header = header;
		this.clickable = clickable;
	}

	public GridColumn(String name, String header, boolean clickable, String cssClass)
	{
		this.name = name;
		this.header = header;
		this.cssClass = cssClass;
		this.clickable = clickable;
	}

	public String getCssClass()
	{
		return cssClass;
	}
	
	public boolean hasCssClass()
	{
		return !StringUtils.isNullOrEmpty(this.cssClass);
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public void setHeader(String header)
	{
		this.header = header;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isClickable()
	{
		return clickable;
	}

	public void setClickable(boolean clickable)
	{
		this.clickable = clickable;
	}

}