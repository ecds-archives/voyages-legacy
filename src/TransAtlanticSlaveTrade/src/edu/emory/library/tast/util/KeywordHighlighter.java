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
package edu.emory.library.tast.util;

import java.util.regex.Pattern;

public class KeywordHighlighter
{
	
	private Pattern regex = null;
	private String replaceString = null;
	
	public KeywordHighlighter(String[] keywords, String tagName, String cssClass)
	{
		
		// nothing to be done
		if (keywords == null || keywords.length == 0)
			return;
		
		// we are assuming that keywords are just
		// simple alpha-numeric strings, which is
		// find for our needs as of now
		regex = Pattern.compile(
				"(" + StringUtils.join("|", keywords) + ")",
				Pattern.CASE_INSENSITIVE);
		
		// ugly concatenation, but done only once
		replaceString =
			"<" + tagName + " class=\"" + cssClass + "\">" +
				"$1" +
			"</" + tagName + ">";

	}
	
	public String highlight(String str)
	{
		if (regex == null)
			return str;
		else
			return regex.matcher(str).replaceAll(replaceString);
	}

}
