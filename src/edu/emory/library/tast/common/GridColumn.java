package edu.emory.library.tast.common;

import edu.emory.library.tast.util.StringUtils;

public class GridColumn
{
	
	private String header;
	private String cssClass;
	
	public GridColumn(String header)
	{
		this.header = header;
	}
	
	public GridColumn(String header, String cssClass)
	{
		this.header = header;
		this.cssClass = cssClass;
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

}
