package edu.emory.library.tast.reditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.StringUtils;

public class FieldValueText extends FieldValue
{
	
	public static final String TYPE = "text";

	private String value;

	public FieldValueText(String name)
	{
		super(name);
	}
	
	public FieldValueText(String name, String value)
	{
		super(name);
		this.value = value;
	}

	public static String getHtmlFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name;
	}

	public void decode(UIComponent component, FacesContext context)
	{
		String htmlFieldName = FieldValueText.getHtmlFieldName(component, context, getName());
		value = (String) context.getExternalContext().getRequestParameterMap().get(htmlFieldName);
	}

	public boolean isEmpty()
	{
		return StringUtils.isNullOrEmpty(value); 
	}
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String[] getLines()
	{
		if (value == null)
		{
			return new String[0];
		}
		else
		{
			return value.split("\n");
		}
	}

}