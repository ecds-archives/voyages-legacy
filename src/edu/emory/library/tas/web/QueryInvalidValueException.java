package edu.emory.library.tas.web;

public class QueryInvalidValueException extends Exception
{

	private static final long serialVersionUID = 1165927964519650387L;
	
	private String attributeName;

	public QueryInvalidValueException(String attributeName)
	{
		super();
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

}
