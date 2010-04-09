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
