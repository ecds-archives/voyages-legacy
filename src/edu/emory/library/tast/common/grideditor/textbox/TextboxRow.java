package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Row;

public class TextboxRow extends Row
{

	public TextboxRow(String name, String label)
	{
		super(TextboxAdapter.TYPE, name, label);
	}

}
