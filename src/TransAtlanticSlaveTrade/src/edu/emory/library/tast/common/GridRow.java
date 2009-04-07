package edu.emory.library.tast.common;

import edu.emory.library.tast.util.StringUtils;

public class GridRow
{
	
	private String[] values;
	private String cssClass;
	private String rowId;
	
	public GridRow(String rowId, String[] values)
	{
		this.rowId = rowId;
		this.values = values;
	}
	
	public String getNthValue(int n)
	{
		return getNthValue(n, "");
	}

	public String getNthValue(int n, String def)
	{
		if (this.values == null || this.values.length <= n || n < 0)
		{
			return def;
		}
		else
		{
			return this.values[n];
		}
	}
	
	public boolean hasCssClass()
	{
		return !StringUtils.isNullOrEmpty(this.cssClass);
	}
	
	public String getCssClass()
	{
		return cssClass;
	}
	
	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}
	
	public String[] getValues()
	{
		return values;
	}
	
	public void setValues(String[] values)
	{
		this.values = values;
	}

	public String getRowId()
	{
		return rowId;
	}

	public void setRowId(String rowId)
	{
		this.rowId = rowId;
	}

}