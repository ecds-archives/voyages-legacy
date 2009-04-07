package edu.emory.library.tast.glossary;

import java.util.ArrayList;

public class GlossaryListLetter
{
	
	private char letter;
	private int totalTermsCount;
	private ArrayList terms = new ArrayList();
	
	public void setLetter(char letter)
	{
		this.letter = letter;
	}

	public void addTerm(String term, String description)
	{
		terms.add(new GlossaryListTerm(term, description));
	}
	
	public char getLetter()
	{
		return letter;
	}
	
	public ArrayList getTerms()
	{
		return terms;
	}

	public int getTotalTermsCount()
	{
		return totalTermsCount;
	}

	public void setTotalTerms(int totalTerms)
	{
		this.totalTermsCount = totalTerms;
	}
	
	public int getMatchesTermsCount()
	{
		return terms.size();
	}

}