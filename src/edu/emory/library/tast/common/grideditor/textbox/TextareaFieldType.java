package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.FieldType;

public class TextareaFieldType extends FieldType
{
	
	public static final int ROWS_DEFAULT = -1;
	private int rows = ROWS_DEFAULT;

	public TextareaFieldType(String name)
	{
		super(name);
	}

	public TextareaFieldType(String name, int rows)
	{
		super(name);
		this.rows = rows;
	}

	public String getType()
	{
		return TextareaAdapter.TYPE;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

}
