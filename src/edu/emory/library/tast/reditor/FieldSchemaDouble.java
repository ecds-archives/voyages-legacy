package edu.emory.library.tast.reditor;


public class FieldSchemaDouble extends FieldSchemaTextbox
{
	
	public FieldSchemaDouble(String name, String description)
	{
		super(name, description);
	}
	
	public String getType()
	{
		return FieldValueDouble.TYPE;
	}

}