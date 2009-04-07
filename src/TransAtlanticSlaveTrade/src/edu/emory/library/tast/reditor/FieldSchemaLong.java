package edu.emory.library.tast.reditor;


public class FieldSchemaLong extends FieldSchemaTextbox
{
	
	public FieldSchemaLong(String name, String description)
	{
		super(name, description, "record-editor-long");
	}
	
	public FieldSchemaLong(String name, String description, String cssClass)
	{
		super(name, description, cssClass);
	}

	public String getType()
	{
		return FieldValueLong.TYPE;
	}

}