package edu.emory.library.tast.util;

public class StringAggregator
{
	
	private String separator = null;
	private StringBuffer buff = new StringBuffer();
	
	public StringAggregator()
	{
	}
	
	public StringAggregator(String separator)
	{
		this.separator = separator;
	}
	
	public void append(String str)
	{
		if (separator != null && buff.length() > 0) buff.append(separator);
		buff.append(str);
	}
	
	public void appendConditionaly(boolean condition, String str)
	{
		if (condition) append(str);
	}

	public void appendIfNotNull(String str)
	{
		if (str != null) append(str);
	}

	public void appendIfNotNullOrEmpty(String str)
	{
		if (!StringUtils.isNullOrEmpty(str)) append(str);
	}

	public void reset()
	{
		buff.setLength(0);
	}
	
	public boolean isEmpty()
	{
		return buff.length() == 0;
	}

	public String toString()
	{
		return buff.toString();
	}
	
}