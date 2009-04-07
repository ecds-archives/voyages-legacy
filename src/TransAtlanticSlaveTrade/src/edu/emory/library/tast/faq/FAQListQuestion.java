package edu.emory.library.tast.faq;

public class FAQListQuestion
{
	
	private String question;
	private String answer;
	
	public FAQListQuestion(String question, String answer)
	{
		this.question = question;
		this.answer = answer;
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

}
