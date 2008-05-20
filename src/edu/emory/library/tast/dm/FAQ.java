package edu.emory.library.tast.dm;

public class FAQ
{
	
	private long id;
	private String question;
	private String answer;
	private FAQCategory category;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getQuestion()
	{
		return question;
	}
	
	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	public String getAnswer()
	{
		return answer;
	}
	
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}

	public FAQCategory getCategory()
	{
		return category;
	}

	public void setCategory(FAQCategory category)
	{
		this.category = category;
	}

}
