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

	public static FieldValue createFieldValue(FieldSchema fieldSchema)
	{
		return FieldValue.createFieldValue(fieldSchema.getName(), fieldSchema.getType());
	}

	public static FieldValue createFieldValue(FieldSchemaState fieldState)
	{
		return FieldValue.createFieldValue(fieldState.getName(), fieldState.getType());
	}

	public static FieldValue createFieldValue(String name, String type)
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
		else if (FieldValueDate.TYPE.equals(type))
		{
			return new FieldValueDate(name);
		}
		else if (FieldValueInteger.TYPE.equals(type))
		{
			return new FieldValueInteger(name);
		}
		else if (FieldValueLong.TYPE.equals(type))
		{
			return new FieldValueLong(name);
		}
		else if (FieldValueFloat.TYPE.equals(type))
		{
			return new FieldValueFloat(name);
		}
		else if (FieldValueDouble.TYPE.equals(type))
		{
			return new FieldValueDouble(name);
		}
		else
		{
			throw new RuntimeException("unsupported field type '" + type + "' for '" + name + "'");
		}
	}
	
}