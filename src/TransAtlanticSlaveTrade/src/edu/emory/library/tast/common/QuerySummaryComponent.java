/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class QuerySummaryComponent extends UIComponentBase
{
	
	private static final int MAX_LENGTH = 20;
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
	
				String itemToShow = null;
				String toolTip = null;
				if (item.getValue().length() > MAX_LENGTH) {
					itemToShow = item.getValue().substring(0, MAX_LENGTH) + " ... ";
					toolTip = item.getValue();
					this.writeToolTip(writer, i, toolTip);
				} else {
					itemToShow = item.getValue();
				}
				writer.startElement("span", this);
				writer.writeAttribute("id", this.getId() + "_span_" + i, null);
				if (toolTip != null) {
					writer.writeAttribute("onmouseover", "showToolTipOff('" + this.getId() + "_tooltip_" + i + "', " + "'" + 
							this.getId() + "_span_" + i + "',300)", null);
					writer.writeAttribute("onmouseout", "hideToolTip('" + this.getId() + "_tooltip_" + i + "')", null);
				}
				writer.writeAttribute("class", "query-summary-value", null);
				writer.write(itemToShow);
				writer.endElement("span");
	
				writer.endElement("div");
				
			}

		}

		writer.endElement("div");
		
	}
	
	public void writeToolTip(ResponseWriter writer, int id, String text) throws IOException {
		writer.startElement("div", this);
		writer.writeAttribute("id", this.getId() + "_tooltip_" + id, null);
		writer.writeAttribute("class", "grid-tooltip", null);
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-11", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-12", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-13", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");								
		writer.endElement("tr");
		
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-21", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-22", null);								
		writer.startElement("div", this);
		writer.write(text);
		writer.endElement("div");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-23", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");								
		writer.endElement("tr");
		
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-31", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-32", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-33", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");								
		writer.endElement("tr");
		writer.endElement("table");
		
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
