package edu.emory.library.tast.reditor;


public class FieldSchemaFloat extends FieldSchemaTextbox
{
	
	public FieldSchemaFloat(String name, String description)
	{
		super(name, description);
	}
	
	public String getType()
	{
		return FieldValueFloat.TYPE;
	}

}