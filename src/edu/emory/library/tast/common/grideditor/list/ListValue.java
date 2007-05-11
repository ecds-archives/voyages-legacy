package edu.emory.library.tast.common.grideditor.list;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class ListValue extends Value
{
	
	private String values[];
	
	public ListValue()
	{
	}

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
	
	public String toString()
	{
		return StringUtils.join("/", values);
	}

	public boolean equals(Object obj)
	{

		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof ListValue))
			return false;

		ListValue that = (ListValue) obj;

		return StringUtils.compareStringArrays(this.values, that.values);
		
	}
	
}