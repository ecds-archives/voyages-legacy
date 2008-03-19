package edu.emory.library.tast.glossary;

public class GlossaryListTerm
{
	
	public String term;
	public String description;
	
	public GlossaryListTerm(String term, String description)
	{
		this.term = term;
		this.description = description;
	}

	public String getTerm()
	{
		return term;
	}
	
	public String getDescription()
	{
		return description;
	}

}
