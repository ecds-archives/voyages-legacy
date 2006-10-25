package edu.emory.library.tast.ui;

import java.io.Serializable;

public class SelectItem implements Serializable
{
	
	private static final long serialVersionUID = -5198351959311658847L;

	private String value;
	private String text;
	private int orderNumber = -1;
	private SelectItem[] subItems = null;
	private boolean selectable = true;
	
	public SelectItem()
	{
	}
	
	public SelectItem(String text, String value)
	{
		this.text = text;
		this.value = value;
		this.orderNumber = -1;
	}

	public SelectItem(String text, String value, int orderNumber)
	{
		this.text = text;
		this.value = value;
		this.orderNumber = orderNumber;
	}
	
	public SelectItem(String text, String value, int orderNumber, SelectItem[] subItems)
	{
		this.text = text;
		this.value = value;
		this.orderNumber = orderNumber;
		this.subItems = subItems;
	}
	
	public boolean hasSubItems()
	{
		return subItems != null && subItems.length != 0;
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

	public int getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public SelectItem[] getSubItems()
	{
		return subItems;
	}

	public void setSubItems(SelectItem[] subItems)
	{
		this.subItems = subItems;
	}

	public String toString()
	{
		return value + ": " + text;
	}

	public boolean isSelectable()
	{
		return selectable;
	}

	public void setSelectable(boolean selectable)
	{
		this.selectable = selectable;
	}

}