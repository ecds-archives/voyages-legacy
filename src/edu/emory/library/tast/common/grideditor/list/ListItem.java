package edu.emory.library.tast.common.grideditor.list;

import edu.emory.library.tast.util.StringUtils;

public class ListItem
{
	
	private String value;
	private String text;
	private ListItem[] subItems;
	
	public ListItem(String value, String text)
	{
		this.value = value;
		this.text = text;
	}

	public ListItem(String value, String text, ListItem[] subItems)
	{
		this.value = value;
		this.text = text;
		this.subItems = subItems;
	}

	public ListItem[] getSubItems()
	{
		if (subItems == null)
		{
			return new ListItem[] {};
		}
		else
		{
			return subItems;
		}
	}
	
	public void setSubItems(ListItem[] subItems)
	{
		this.subItems = subItems;
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
	
	public boolean hasSubItems()
	{
		return subItems != null && subItems.length != 0;
	}
	
	public ListItem getFirstSubItem()
	{
		if (subItems != null && subItems.length > 0)
		{
			return subItems[0];
		}
		else
		{
			return null;
		}
	}
	
	public static ListItem findSubItemByValue(ListItem[] items, String value)
	{
		if (items != null)
		{
			for (int i = 0; i < items.length; i++)
			{
				ListItem item = items[i];
				if (StringUtils.compareStrings(item.getValue(), value))
				{
					return item; 
				}
			}
		}
		return null;
	}
	
	public ListItem findSubItemByValue(String value)
	{
		return ListItem.findSubItemByValue(subItems, value);
	}
	
	public static int determineMaxDepth(ListItem[] items)
	{
		int max = 0;
		if (items != null)
		{
			for (int i = 0; i < items.length; i++)
			{
				max = Math.max(items[i].determineMaxDepth(), max);
			}
		}
		return max;
	}
	
	public int determineMaxDepth()
	{
		int max = 1;
		if (subItems != null && subItems.length > 0)
		{
			for (int i = 0; i < subItems.length; i++)
			{
				max = Math.max(subItems[i].determineMaxDepth() + 1, max);
			}
		}
		return max;
	}
	
}