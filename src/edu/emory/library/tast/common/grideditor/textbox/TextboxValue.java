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
	
	public boolean equals(Object obj)
	{
		
		if (obj == this)
			return true;
		
		if (obj == null)
			return false;
		
		if (!(obj instanceof TextboxValue))
			return false;
		
		TextboxValue that = (TextboxValue) obj;
		
		return StringUtils.compareStrings(this.text, that.text, true);
		
	}

	public boolean isCorrectValue() {
		return true;
	}

	public boolean isEmpty() {
		return text == null || text.length() == 0;
	}
}