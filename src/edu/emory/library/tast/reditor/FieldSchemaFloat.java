package edu.emory.library.tast.reditor;


public class FieldSchemaFloat extends FieldSchemaTextbox
{
	
	public FieldSchemaFloat(String name, String description)
	{
		super(name, description, "record-editor-float");
	}
	
	public FieldSchemaFloat(String name, String description, String cssClass)
	{
		super(name, description, cssClass);
	}

	public String getType()
	{
		return FieldValueFloat.TYPE;
	}

}