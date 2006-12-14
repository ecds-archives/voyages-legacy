package edu.emory.library.tast.reditor;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

public abstract class FieldSchema
{
	
	private String name;
	private String description;
	private String label;
	
	public abstract void encode(EditorComponent editor, UIForm form, FacesContext context, Schema schema, FieldValue value) throws IOException;

	public FieldSchema(String name, String label)
	{
		this.name = name;
		this.label = label;
	}

	public FieldSchema(String name, String label, String description)
	{
		this.name = name;
		this.label = label;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public boolean hasDescription()
	{
		return description != null && description.length() > 0;
	}
	
}