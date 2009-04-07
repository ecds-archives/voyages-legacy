package edu.emory.library.tast.common;

import java.io.Serializable;

public class SimpleListStyle implements Serializable
{

	private static final long serialVersionUID = 2445965207252391047L;
	
	private static final int STYLE_PLAIN = 1;
	private static final int STYLE_UL = 2;
	private static final int STYLE_OL = 3;
	
	public static final SimpleListStyle Plain = new SimpleListStyle(STYLE_PLAIN); 
	public static final SimpleListStyle UnorderedList = new SimpleListStyle(STYLE_UL); 
	public static final SimpleListStyle OrderedList = new SimpleListStyle(STYLE_OL); 
	
	private int mode = 0;

	private SimpleListStyle(int mode)
	{
		this.mode = mode;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof SimpleListStyle)
		{
			return ((SimpleListStyle)obj).mode == mode;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		switch (mode)
		{
			case STYLE_UL: return "ul";
			case STYLE_OL: return "ol";
			default: return "plain";
		}
	}
	
	public static SimpleListStyle parse(String value)
	{
		if (value != null && value.equals("ul"))
			return UnorderedList;
		else if (value != null && value.equals("ol"))
			return OrderedList;
		else
			return Plain;
	}
	
}
