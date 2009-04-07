package edu.emory.library.tast.common.grideditor.textbox;


public class TextboxFieldType extends AbstractTextFieldType
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

	public TextboxFieldType(String name, String cssClass)
	{
		super(name, cssClass);
	}

	public TextboxFieldType(String name, String cssClass, String cssStyle)
	{
		super(name, cssClass, cssStyle);
	}

	public TextboxFieldType(String name, int maxLength)
	{
		super(name);
		this.maxLength = maxLength;
	}

	public TextboxFieldType(String name, String cssClass, int maxLength)
	{
		super(name, cssClass);
		this.maxLength = maxLength;
	}

	public TextboxFieldType(String name, String cssClass, String cssStyle, int maxLength)
	{
		super(name, cssClass, cssStyle);
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
