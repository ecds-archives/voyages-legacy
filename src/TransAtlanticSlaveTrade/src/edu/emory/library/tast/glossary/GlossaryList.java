package edu.emory.library.tast.glossary;

import java.util.ArrayList;

public class GlossaryList
{
	
	private ArrayList letters;
	private boolean listingAll = true;
	private String[] keywords;
	
	public GlossaryListLetter addLetter(char letter)
	{
		GlossaryListLetter letterDesc = new GlossaryListLetter();
		letterDesc.setLetter(letter);
		getLetters().add(letterDesc);
		return letterDesc;
	}

	public ArrayList getLetters()
	{
		if (letters == null) letters = new ArrayList();
		return letters;
	}

	public void setLetters(ArrayList letters)
	{
		this.letters = letters;
	}

	public boolean isListingAll()
	{
		return listingAll;
	}

	public void setListingAll(boolean listingAll)
	{
		this.listingAll = listingAll;
	}

	public String[] getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String[] keywords)
	{
		this.keywords = keywords;
	}

}
