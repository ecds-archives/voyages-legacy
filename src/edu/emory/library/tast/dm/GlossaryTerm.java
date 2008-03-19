package edu.emory.library.tast.dm;


public class GlossaryTerm
{
	
	private long id;
	private String term;
	private String description;
	
	public GlossaryTerm()
	{
	}

	public long getId()
	{
		return id;
	}

	public void setId(long termId)
	{
		this.id = termId;
	}

	public String getTerm()
	{
		return term;
	}
	
	public void setTerm(String term)
	{
		this.term = term;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

}
