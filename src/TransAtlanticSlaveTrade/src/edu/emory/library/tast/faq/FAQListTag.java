package edu.emory.library.tast.faq;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class FAQListTag extends UIComponentTag
{
	
	private String faqList;

	public String getComponentType()
	{
		return "FAQList";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		super.setProperties(component);
		
		FAQListComponent glossaryList = (FAQListComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (faqList != null && isValueReference(faqList))
		{
			ValueBinding vb = app.createValueBinding(faqList);
			glossaryList.setValueBinding("faqList", vb);
		}
		
	}

	public String getFaqList()
	{
		return faqList;
	}

	public void setFaqList(String faqList)
	{
		this.faqList = faqList;
	}

}
