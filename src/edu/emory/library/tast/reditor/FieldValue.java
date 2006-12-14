package edu.emory.library.tast.reditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public abstract class FieldValue
{
	
	private String name;
	
	public abstract void decode(UIComponent component, FacesContext context);

	public FieldValue(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public static FieldValue createFieldValue(FieldSchemaState fieldState)
	{
		return FieldValue.createFieldValue(fieldState.getName(), fieldState.getType());
	}

	public static FieldValue createFieldValue(String type, String name)
	{
		if (type == null)
		{
			throw new RuntimeException("field type null");
		}
		else if (FieldValueText.TYPE.equals(type))
		{
			return new FieldValueText(name);
		}
		else if (FieldValueCheckbox.TYPE.equals(type))
		{
			return new FieldValueCheckbox(name);
		}
		else if (FieldValueDropdowns.TYPE.equals(type))
		{
			return new FieldValueDropdowns(name);
		}
		else
		{
			throw new RuntimeException("unsupported field type");
		}
	}
	
}
