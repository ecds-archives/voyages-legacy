package edu.emory.library.tast.glossary;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GlossaryLettersTag extends UIComponentTag
{
	
	private String letters;
	private String glossaryListId;

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
		
		GlossaryLettersComponent glossaryLetters = (GlossaryLettersComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (letters != null && isValueReference(letters))
		{
			ValueBinding vb = app.createValueBinding(letters);
			glossaryLetters.setValueBinding("letters", vb);
		}
		
		if (glossaryListId != null && isValueReference(glossaryListId))
		{
			ValueBinding vb = app.createValueBinding(glossaryListId);
			glossaryLetters.setValueBinding("glossaryListId", vb);
		}
		else
		{
			glossaryLetters.setGlossaryListId(glossaryListId);
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

	public String getGlossaryListId()
	{
		return glossaryListId;
	}

	public void setGlossaryListId(String glossaryListId)
	{
		this.glossaryListId = glossaryListId;
	}

}

