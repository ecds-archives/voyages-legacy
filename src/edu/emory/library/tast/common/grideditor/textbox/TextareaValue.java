package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class TextareaValue extends Value
{
	
	private String[] texts;

	public TextareaValue(String[] texts)
	{
		this.texts = texts;
	}

	public TextareaValue(String text)
	{
		texts = new String[] {text};
	}

	public String getText()
	{
		StringBuffer buffer = new StringBuffer();
		if (texts != null) {
			for (int i = 0; i < texts.length; i++) {
				if (!StringUtils.isNullOrEmpty(texts[i])) {
					buffer.append(texts[i]).append("\n");
				}
			}
		}
		return buffer.toString();
	}

	public void setText(String[] texts)
	{
		this.texts = texts;
	}
	
	public boolean isEmptyOrNull()
	{
		return texts == null;
	}

	public String[] getTexts()
	{
		return texts;
	}

	public String toString()
	{
		return StringUtils.join("\n", texts);
	}

	public boolean equals(Object obj)
	{

		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof TextareaValue))
			return false;

		TextareaValue that = (TextareaValue) obj;

		return StringUtils.compareStringArrays(this.texts, that.texts);
		
	}
	
	public boolean isCorrectValue() {
		return true;
	}

}