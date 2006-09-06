package edu.emory.library.tast;

import java.util.Comparator;

public class LanguageComparator implements Comparator
{
	public int compare(Object arg0, Object arg1)
	{
		Language lang0 = (Language) arg0;
		Language lang1 = (Language) arg1;
		return lang0.getName().compareToIgnoreCase(lang1.getName());
	}
}
