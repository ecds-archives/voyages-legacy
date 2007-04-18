package edu.emory.library.tast.common.grideditor.list;

import edu.emory.library.tast.common.grideditor.Value;

public class ListValue extends Value
{
	
	private String values[];
	
	public ListValue(String[] values)
	{
		this.values = values;
	}

	public String[] getValues()
	{
		return values;
	}

	public void setValues(String[] values)
	{
		this.values = values;
	}

}