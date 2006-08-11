package edu.emory.library.tast.ui;

import java.io.Serializable;

public class SelectItem implements Serializable
{
	
	private static final long serialVersionUID = -5198351959311658847L;

	private String value;
	private String text;
	private int orderNumber = -1;
	
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
	
	public String toString()
	{
		return value + ": " + text;
	}

}
