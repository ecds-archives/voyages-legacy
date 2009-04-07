package edu.emory.library.tast.reditor;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class EditorTag extends UIComponentTag
{
	
	private String schema;
	private String values;

	public String getComponentType()
	{
		return "RecordEditor";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (schema != null && isValueReference(schema))
		{
			ValueBinding vb = app.createValueBinding(schema);
			component.setValueBinding("schema", vb);
		}
		
		if (values != null && isValueReference(values))
		{
			ValueBinding vb = app.createValueBinding(values);
			component.setValueBinding("values", vb);
		}

	}

	public String getSchema()
	{
		return schema;
	}

	public void setSchema(String schema)
	{
		this.schema = schema;
	}

	public String getValues()
	{
		return values;
	}

	public void setValues(String values)
	{
		this.values = values;
	}

}
