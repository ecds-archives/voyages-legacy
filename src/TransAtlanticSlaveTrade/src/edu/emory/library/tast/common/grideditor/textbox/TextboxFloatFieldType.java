package edu.emory.library.tast.common.grideditor.textbox;


public class TextboxFloatFieldType extends TextboxFieldType
{

	public TextboxFloatFieldType(String name)
	{
		super(name);
	}

	public String getType()
	{
		return TextboxFloatAdapter.TYPE;
	}

}
