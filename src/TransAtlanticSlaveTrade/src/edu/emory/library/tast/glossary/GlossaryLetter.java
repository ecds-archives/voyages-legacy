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


public class GlossaryLetter implements Comparable
{
	
	private char letter;
	private boolean matched;
	
	public GlossaryLetter(char letter, boolean covered)
	{
		this.letter = letter;
		this.matched = covered;
	}

	public char getLetter()
	{
		return letter;
	}

	public boolean isMatched()
	{
		return matched;
	}

	public void setMatched(boolean covered)
	{
		this.matched = covered;
	}

	public int compareTo(Object otherObj)
	{
		if (!(otherObj instanceof GlossaryLetter)) throw new RuntimeException("expected GlossaryLetter to compare");
		return new Character(letter).compareTo((Character) otherObj);
	}

}
