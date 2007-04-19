package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Value;

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
				buffer.append(texts[i]).append("\n");
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

	public String[] getTexts() {
		return texts;
	}


}