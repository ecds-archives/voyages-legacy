package edu.emory.library.tast.reditor;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FieldValueDropdowns extends FieldValue
{
	
	public static final String TYPE = "dropdowns";
	
	private String values[];
	
	public FieldValueDropdowns(String name)
	{
		super(name);
	}
	
	public FieldValueDropdowns(String name, String values[])
	{
		super(name);
		this.values = values;
	}

	public static String getHtmlCountHiddenFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name + "_count";
	}

	public static String getHtmlSelectName(UIComponent component, FacesContext context, String name, int listIndex)
	{
		return component.getClientId(context) + "_" + name + "_" + listIndex;
	}
	
	public String[] getValues()
	{
		return values;
	}

	public void setValues(String[] values)
	{
		this.values = values;
	}
	
	public String getValue(int index)
	{
		if (values == null || index < 0 || values.length <= index)
		{
			return null;
		}
		else
		{
			return values[index];
		}
	}

	public void decode(UIComponent component, FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();

		String countFieldName = FieldValueDropdowns.getHtmlCountHiddenFieldName(component, context, getName());
		int count = Integer.parseInt((String) params.get(countFieldName));
		
		values = new String[count];
		
		for (int i = 0; i < count; i++)
		{
			String htmlFieldName = FieldValueDropdowns.getHtmlSelectName(component, context, getName(), i);
			values[i] = (String) params.get(htmlFieldName);
		}
		
	}

}