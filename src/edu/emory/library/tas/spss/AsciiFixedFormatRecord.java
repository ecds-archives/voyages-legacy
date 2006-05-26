package edu.emory.library.tas.spss;

public class AsciiFixedFormatRecord extends Record
{
	
	private char[] line;
	private String key;
	
	//public int bufPtr;
	
	public AsciiFixedFormatRecord(String key, char[] line)
	{
		this.line = line;
		this.key = key;
		//this.bufPtr = bufPtr;
	}
	
	public int compareTo(Record record)
	{
		return this.key.compareTo(((AsciiFixedFormatRecord)record).getKey());
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

}
