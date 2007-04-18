package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Row;

public class TexboxLongRow extends Row
{

	public TexboxLongRow(String name, String label, String description)
	{
		super(TextboxLongAdapter.TYPE, name, label, description);
	}

	public TexboxLongRow(String name, String label)
	{
		super(TextboxLongAdapter.TYPE, name, label);
	}

}