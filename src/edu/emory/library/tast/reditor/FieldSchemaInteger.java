package edu.emory.library.tast.reditor;


public class FieldSchemaInteger extends FieldSchemaTextbox
{
	
	public FieldSchemaInteger(String name, String description)
	{
		super(name, description);
	}
	
	public String getType()
	{
		return FieldValueInteger.TYPE;
	}

}