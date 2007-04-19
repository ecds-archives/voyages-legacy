package edu.emory.library.tast.common.grideditor.textbox;


public class TextareaFieldType extends AbstractTextFieldType
{
	
	public static final int ROWS_DEFAULT = -1;
	private int rows = ROWS_DEFAULT;

	public TextareaFieldType(String name, int rows)
	{
		super(name);
		this.rows = rows;
	}

	public TextareaFieldType(String name, String cssClass, int rows)
	{
		super(name, cssClass);
		this.rows = rows;
	}

	public TextareaFieldType(String name, String cssClass, String cssStyle, int rows)
	{
		super(name, cssClass, cssStyle);
		this.rows = rows;
	}

	public TextareaFieldType(String name)
	{
		super(name);
	}

	public TextareaFieldType(String name, String cssClass)
	{
		super(name, cssClass);
	}

	public TextareaFieldType(String name, String cssClass, String cssStyle)
	{
		super(name, cssClass, cssStyle);
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
