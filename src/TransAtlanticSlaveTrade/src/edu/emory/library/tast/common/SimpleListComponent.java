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

	private boolean listStyleSet = false;
	private SimpleListStyle listStyle = SimpleListStyle.Plain;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = listStyle;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		listStyle = (SimpleListStyle) values[1];
	}
	
	private void renderLevel(ResponseWriter writer, SimpleListElement[] elements, SimpleListStyle listStyle, int level) throws IOException
	{
		
		String itemHtmlElement =
			listStyle.equals(SimpleListStyle.Plain) ? "div" :
				"li";
		
		String mainHtmlElement = 		
			listStyle.equals(SimpleListStyle.UnorderedList) ? "ul" : 
				listStyle.equals(SimpleListStyle.OrderedList) ? "ol" :
					"div";
		
		writer.startElement(mainHtmlElement, this);
		
		for (int i = 0; i < elements.length; i++)
		{
			SimpleListElement element = elements[i];
			if (element.getText() != null) 
			{
				writer.startElement(itemHtmlElement, this);
				if (element.getCssClass() != null) writer.writeAttribute("class", element.getCssClass(), null);
				if (element.getCssStyle() != null) writer.writeAttribute("style", element.getCssStyle(), null);
				writer.write(element.getText());
				writer.endElement(itemHtmlElement);
				if (element.hasSubelements())
				{
					renderLevel(writer, element.getSubElements(), listStyle, level+1);
				}
			}
		}
		
		writer.endElement(mainHtmlElement);

	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		listStyle = getListStyle();
		
		ResponseWriter writer = context.getResponseWriter();
		
		SimpleListElement[] elements = getElements();
		if (elements != null) renderLevel(writer, elements, listStyle, 0);
	
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

	public SimpleListStyle getListStyle()
	{
		return (SimpleListStyle) JsfUtils.getCompPropObject(this, getFacesContext(),
				"listStyle", listStyleSet, listStyle);
	}

	public void setListStyle(SimpleListStyle listStyle)
	{
		listStyleSet = true;
		this.listStyle = listStyle;
	}

}