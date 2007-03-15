package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class QuerySummaryComponent extends UIComponentBase
{
	
	private boolean itemsSet = false;
	private List items;
	
	private boolean noQueryTextSet = false;
	private String noQueryText;

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{

		ResponseWriter writer = context.getResponseWriter();
		
		items = getItems();
		noQueryText = getNoQueryText();
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "query-summary", null);
		
		if (items == null || items.size() == 0)
		{

			writer.write(noQueryText);
			
		}
		else
		{
			
			int itemsCount = items.size();

			for (int i = 0; i < itemsCount; i++)
			{	
				
				QuerySummaryItem item = (QuerySummaryItem) items.get(i);
				
				writer.startElement("div", this);
				if (i < itemsCount - 1)
					writer.writeAttribute("class", "query-summary-item", null);
				else
					writer.writeAttribute("class", "query-summary-item-last", null);
				
				writer.startElement("span", this);
				writer.writeAttribute("class", "query-summary-variable", null);
				writer.write(item.getVariable());
				writer.endElement("span");
				
				writer.write(": ");
	
				writer.startElement("span", this);
				writer.writeAttribute("class", "query-summary-value", null);
				writer.write(item.getValue());
				writer.endElement("span");
	
				writer.endElement("div");
				
			}

		}

		writer.endElement("div");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public List getItems()
	{
		return (List) JsfUtils.getCompPropObject(this, getFacesContext(),
				"items", itemsSet, items);
	}

	public void setItems(List items)
	{
		itemsSet = true;
		this.items = items;
	}

	public String getNoQueryText()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"noQueryText", noQueryTextSet, noQueryText);
	}

	public void setNoQueryText(String noQueryText)
	{
		noQueryTextSet = true;
		this.noQueryText = noQueryText;
	}
	
}
