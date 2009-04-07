package edu.emory.library.tast.reditor;


public class FieldSchemaInteger extends FieldSchemaTextbox
{
	
	public FieldSchemaInteger(String name, String description)
	{
		super(name, description, "record-editor-integer");
	}
	
	public FieldSchemaInteger(String name, String description, String cssClass)
	{
		super(name, description, cssClass);
	}

	public String getType()
	{
		return FieldValueInteger.TYPE;
	}

}