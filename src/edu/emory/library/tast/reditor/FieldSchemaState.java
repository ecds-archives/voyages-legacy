package edu.emory.library.tast.reditor;

import java.io.Serializable;

public class FieldSchemaState implements Serializable
{
	
	private static final long serialVersionUID = -7115326661779343431L;

	private String name;
	private String type;
	
	public FieldSchemaState(FieldSchema field)
	{
		this.name = field.getName();
		this.type = field.getType();
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}

}