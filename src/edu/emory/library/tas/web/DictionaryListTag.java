package edu.emory.library.tas.web;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class DictionaryListTag extends UIComponentTag
{
	
	private String attributeName;
	private String formName;
	private String hiddenFieldName;
	private String displayFieldName;
	private String dictionary;
	
	protected void setProperties(UIComponent component)
	{

		Application app = FacesContext.getCurrentInstance().getApplication();
		DictionaryListComponent dictionaryList = (DictionaryListComponent) component;
		
		if (attributeName != null && isValueReference(attributeName))
		{
			ValueBinding vb = app.createValueBinding(attributeName);
			component.setValueBinding("attributeName", vb);
		}
		else
		{
			dictionaryList.setAttributeName(attributeName);
		}

		if (formName != null && isValueReference(formName))
		{
			ValueBinding vb = app.createValueBinding(formName);
			component.setValueBinding("formName", vb);
		}
		else
		{
			dictionaryList.setAttributeName(formName);
		}

		if (hiddenFieldName != null && isValueReference(hiddenFieldName))
		{
			ValueBinding vb = app.createValueBinding(hiddenFieldName);
			component.setValueBinding("hiddenFieldName", vb);
		}
		else
		{
			dictionaryList.setAttributeName(hiddenFieldName);
		}

		if (displayFieldName != null && isValueReference(displayFieldName))
		{
			ValueBinding vb = app.createValueBinding(displayFieldName);
			component.setValueBinding("displayFieldName", vb);
		}
		else
		{
			dictionaryList.setAttributeName(displayFieldName);
		}

		if (dictionary != null && isValueReference(dictionary))
		{
			ValueBinding vb = app.createValueBinding(dictionary);
			component.setValueBinding("dictionary", vb);
		}

	}

	public String getComponentType()
	{
		return "DictionaryList";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getDictionary()
	{
		return dictionary;
	}

	public void setDictionary(String list)
	{
		this.dictionary = list;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public String getDisplayFieldName()
	{
		return displayFieldName;
	}

	public void setDisplayFieldName(String displayFieldName)
	{
		this.displayFieldName = displayFieldName;
	}

	public String getFormName()
	{
		return formName;
	}

	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	public String getHiddenFieldName()
	{
		return hiddenFieldName;
	}

	public void setHiddenFieldName(String hiddenFieldName)
	{
		this.hiddenFieldName = hiddenFieldName;
	}

}
