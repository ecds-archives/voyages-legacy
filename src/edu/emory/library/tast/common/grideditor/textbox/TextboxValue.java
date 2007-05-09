package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class TextboxValue extends Value
{
	
	private String text;

	public TextboxValue(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public boolean isEmptyOrNull()
	{
		return StringUtils.isNullOrEmpty(text);
	}
	
	public String toString()
	{
		return text;
	}

}