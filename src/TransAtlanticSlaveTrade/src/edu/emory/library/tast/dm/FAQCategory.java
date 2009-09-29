package edu.emory.library.tast.dm;

import java.util.Set;

public class FAQCategory
{
	
	private long id;
	private String name;
	private Set questions;
	private int order;

	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public Set getQuestions()
	{
		return questions;
	}
	
	public void setQuestions(Set questions)
	{
		this.questions = questions;
	}

	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FAQCategory other = (FAQCategory) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
