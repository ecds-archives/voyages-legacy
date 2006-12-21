package edu.emory.library.tast.reditor;


public class FieldSchemaDouble extends FieldSchemaTextbox
{
	
	public FieldSchemaDouble(String name, String description)
	{
		super(name, description, "record-editor-double");
	}
	
	public FieldSchemaDouble(String name, String description, String cssClass)
	{
		super(name, description, cssClass);
	}

	public String getType()
	{
		return FieldValueDouble.TYPE;
	}

}