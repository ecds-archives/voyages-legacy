package edu.emory.library.tast.common.grideditor.textbox;

public class TextboxLongFieldType extends TextboxFieldType
{

	public TextboxLongFieldType(String name)
	{
		super(name);
	}
	
	public String getType()
	{
		return TextboxLongAdapter.TYPE;
	}

}
