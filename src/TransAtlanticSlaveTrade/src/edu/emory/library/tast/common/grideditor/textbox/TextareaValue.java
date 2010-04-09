/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class TextareaValue extends Value
{
	
	private String[] texts;
	private String[] rollovers;
	
	public TextareaValue(String[] texts)
	{
		
		int n = 0;
		for (int i = 0; i < texts.length; i++)
			if (!StringUtils.isNullOrEmpty(texts[i])) n++;
		
		int j = 0;
		this.texts = new String[n];
		for (int i = 0; i < texts.length; i++)
			if (!StringUtils.isNullOrEmpty(texts[i]))
				this.texts[j++] = texts[i];

	}

	public TextareaValue(String text)
	{
		this(new String[] {text});
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

		return StringUtils.compareStringArrays(this.texts, that.texts, true);
		
	}
	
	public boolean isCorrectValue() {
		return true;
	}

	public boolean isEmpty() {
		return texts == null || texts.length == 0;
	}

	public String[] getRollovers() {
		return rollovers;
	}

	public void setRollovers(String[] rollovers) {
		this.rollovers = rollovers;
	}

}