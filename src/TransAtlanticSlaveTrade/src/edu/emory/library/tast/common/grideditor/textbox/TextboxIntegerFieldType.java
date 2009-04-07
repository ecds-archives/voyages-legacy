package edu.emory.library.tast.common.grideditor.textbox;


public class TextboxIntegerFieldType extends TextboxFieldType
{

	public TextboxIntegerFieldType(String name)
	{
		super(name);
	}

	public String getType()
	{
		return TextboxIntegerAdapter.TYPE;
	}

}
