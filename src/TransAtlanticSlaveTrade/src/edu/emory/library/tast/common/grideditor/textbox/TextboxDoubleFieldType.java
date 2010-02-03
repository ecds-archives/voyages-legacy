package edu.emory.library.tast.common.grideditor.textbox;


public class TextboxDoubleFieldType extends TextboxFieldType
{
	
	public TextboxDoubleFieldType(String name)
	{
		super(name);
		this.inputSize = "10";
	}

	public String getType()
	{
		return TextboxDoubleAdapter.TYPE;
	}

}