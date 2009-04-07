package edu.emory.library.tast.common.grideditor.date;

import edu.emory.library.tast.common.grideditor.FieldType;

public class DateFieldType extends FieldType
{

	public DateFieldType(String name)
	{
		super(name);
	}

	public String getType()
	{
		return DateAdapter.TYPE;
	}

}
