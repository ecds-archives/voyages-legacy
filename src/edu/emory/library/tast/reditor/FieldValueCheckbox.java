package edu.emory.library.tast.reditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FieldValueCheckbox extends FieldValue
{

	public static final String TYPE = "checkbox";
	
	private boolean checked;

	public FieldValueCheckbox(String name)
	{
		super(name);
	}

	public static String getHtmlFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name;
	}

	public void decode(UIComponent component, FacesContext context)
	{
		String htmlFieldName = getHtmlFieldName(component, context, getName());
		checked = context.getExternalContext().getRequestParameterMap().containsKey(htmlFieldName);
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

}
