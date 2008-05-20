package edu.emory.library.tast.faq;

import java.util.LinkedList;
import java.util.List;

public class FAQListCategory
{
	
	private String name;
	private List questions = new LinkedList();

	public FAQListCategory(String name)
	{
		this.name = name;
	}
	
	public FAQListQuestion addQuestion(String question, String answer)
	{
		FAQListQuestion faq = new FAQListQuestion(question, answer);
		questions.add(faq);
		return faq;
	}

	public List getQuestions()
	{
		return questions;
	}

	public void setQuestions(List questions)
	{
		this.questions = questions;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
