package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.FieldType;

public class TextboxFieldType extends FieldType
{
	
	private int maxLength = Integer.MAX_VALUE;

	public TextboxFieldType(String name)
	{
		super(name);
	}

	public String getType()
	{
		return TextboxAdapter.TYPE;
	}

	public TextboxFieldType(String name, int maxLength)
	{
		super(name);
		this.maxLength = maxLength;
	}

	public int getMaxLength()
	{
		return maxLength;
	}

	public void setMaxLength(int maxLength)
	{
		this.maxLength = maxLength;
	}

}
