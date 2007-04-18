package edu.emory.library.tast.common.grideditor.list;

import edu.emory.library.tast.common.grideditor.Row;

public class ListRow extends Row
{
	
	private String listName;

	public ListRow(String name, String label, String listName)
	{
		super(ListAdapter.TYPE, name, label);
		this.listName = listName;
	}

	public String getListName()
	{
		return listName;
	}

}