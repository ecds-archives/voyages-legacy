package edu.emory.library.tast.spss;

public class STSchemaVariableLabel
{
	
	private int key;
	private String label;
	
	public STSchemaVariableLabel()
	{
	}

	public STSchemaVariableLabel(int key, String label)
	{
		this.key = key;
		this.label = label;
	}
	
	public void setKey(int key)
	{
		this.key = key;
	}
	
	public int getKey()
	{
		return key;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}

}