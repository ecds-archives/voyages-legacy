package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Row;

public class TextboxIntegerRow extends Row
{

	public TextboxIntegerRow(String name, String label)
	{
		super(TextboxIntegerAdapter.TYPE, name, label);
	}

}
