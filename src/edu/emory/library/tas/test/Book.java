package edu.emory.library.tas.test;

public class Book
{
	
	private Integer id;
	private String name;
	private Author author;
	
	public Integer getId()
	{
		return id;
	}
	
	public void setId(Integer id)
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

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public Author getAuthor()
	{
		return author;
	}

}
