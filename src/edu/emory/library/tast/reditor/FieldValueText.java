package edu.emory.library.tast.reditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FieldValueText extends FieldValue
{
	
	public static final String TYPE = "text";

	private String value;

	public FieldValueText(String name)
	{
		super(name);
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

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}