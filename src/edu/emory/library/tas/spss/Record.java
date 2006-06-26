package edu.emory.library.tas.spss;

public class Record
{
	protected char[] line;
	protected String key;

	public Record(String key, char[] line)
	{
		this.line = line;
		this.key = key;
	}
	
	public int compareTo(Record record)
	{
		return this.key.compareTo(record.getKey());
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	public void setLine(char[] line)
	{
		this.line = line;
	}

	public char[] getLine()
	{
		return line;
	}
	
	public String getValue(STSchemaVariable var)
	{
		return new String(line, var.getStartColumn()-1, var.getLength()).trim();
	}
	
}
