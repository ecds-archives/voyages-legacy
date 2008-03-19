package edu.emory.library.tast.glossary;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GlossaryListTag extends UIComponentTag
{
	
	private String terms;

	public String getComponentType()
	{
		return "GlossaryList";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		GlossaryListComponent glossaryList = (GlossaryListComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (terms != null && isValueReference(terms))
		{
			ValueBinding vb = app.createValueBinding(terms);
			glossaryList.setValueBinding("terms", vb);
		}
		
	}

	public String getTerms()
	{
		return terms;
	}

	public void setTerms(String terms)
	{
		this.terms = terms;
	}


}
