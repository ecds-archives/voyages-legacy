package edu.emory.library.tast.common.grideditor.list;

import edu.emory.library.tast.common.grideditor.FieldType;

public class ListFieldType extends FieldType
{
	
	private ListItem[] listItems;
	private String separator = " / ";

	public ListFieldType(String name)
	{
		super(name);
	}

	public ListFieldType(String name, ListItem[] listItems)
	{
		super(name);
		this.listItems = listItems;
	}

	public ListFieldType(String name, ListItem[] listItems, String separator)
	{
		super(name);
		this.listItems = listItems;
		this.separator = separator;
	}

	public String getType()
	{
		return ListAdapter.TYPE;
	}

	public ListItem[] getListItems()
	{
		return listItems;
	}

	public void setListItems(ListItem[] listItems)
	{
		this.listItems = listItems;
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

}