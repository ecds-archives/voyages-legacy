package edu.emory.library.tast.reditor;


public class FieldSchemaLong extends FieldSchemaTextbox
{
	
	public FieldSchemaLong(String name, String description)
	{
		super(name, description);
	}
	
	public String getType()
	{
		return FieldValueLong.TYPE;
	}

}