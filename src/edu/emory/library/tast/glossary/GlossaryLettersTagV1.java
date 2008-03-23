package edu.emory.library.tast.glossary;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GlossaryLettersTagV1 extends UIComponentTag
{
	
	private String letters;
	private String columns;

	public String getComponentType()
	{
		return "GlossaryLetters";
	}

	public String getRendererType()
	{
		return null;
	}

	protected void setProperties(UIComponent component)
	{
		
		GlossaryLettersComponentV1 glossaryLetters = (GlossaryLettersComponentV1) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (letters != null && isValueReference(letters))
		{
			ValueBinding vb = app.createValueBinding(letters);
			glossaryLetters.setValueBinding("letters", vb);
		}
		
		if (columns != null && isValueReference(columns))
		{
			ValueBinding vb = app.createValueBinding(columns);
			glossaryLetters.setValueBinding("columns", vb);
		}
		else
		{
		}
		
	}

	public String getLetters()
	{
		return letters;
	}

	public void setLetters(String letters)
	{
		this.letters = letters;
	}

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(String columns)
	{
		this.columns = columns;
	}

}

