package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class SimpleListComponent extends UIComponentBase
{
	
	private boolean elementsSet = false;
	private SimpleListElement[] elements;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		
		SimpleListElement[] elements = getElements();
		
		for (int i = 0; i < elements.length; i++)
		{
			SimpleListElement element = elements[i];
			if (element.getText() != null) 
			{
				writer.startElement("div", this);
				if (element.getCssClass() != null) writer.writeAttribute("class", element.getCssClass(), null);
				if (element.getCssStyle() != null) writer.writeAttribute("style", element.getCssStyle(), null);
				writer.writeAttribute("style", element.getCssStyle(), null);
				writer.write(element.getText());
				writer.endElement("div");
			}
		}
	
	}

	public SimpleListElement[] getElements()
	{
		return (SimpleListElement[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"elements", elementsSet, elements);
	}

	public void setElements(SimpleListElement[] elements)
	{
		elementsSet = true;
		this.elements = elements;
	}

}