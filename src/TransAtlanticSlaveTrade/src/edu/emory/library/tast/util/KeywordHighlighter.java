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
