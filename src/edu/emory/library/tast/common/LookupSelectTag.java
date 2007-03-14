package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class LookupSelectTag extends UIComponentTag
{
	
	private String sourceId;
	private String selectedValues;

	public String getComponentType()
	{
		return "LookupSelect";
	}
	
	protected void setProperties(UIComponent component)
	{
		
		LookupSelectComponent lookupComponent = (LookupSelectComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (selectedValues != null && isValueReference(selectedValues))
		{
			ValueBinding vb = app.createValueBinding(selectedValues);
			component.setValueBinding("selectedValues", vb);
		}
		
		if (sourceId != null && isValueReference(sourceId))
		{
			ValueBinding vb = app.createValueBinding(sourceId);
			component.setValueBinding("sourceId", vb);
		}
		else
		{
			lookupComponent.setSourceId(sourceId);
		}

	}

	public String getRendererType()
	{
		return null;
	}

	public String getSelectedValues()
	{
		return selectedValues;
	}

	public void setSelectedValues(String selectedIds)
	{
		this.selectedValues = selectedIds;
	}

	public String getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(String sourceId)
	{
		this.sourceId = sourceId;
	}

}