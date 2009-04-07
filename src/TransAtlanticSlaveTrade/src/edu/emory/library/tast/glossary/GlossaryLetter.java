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
