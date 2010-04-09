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

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class SimpleBoxComponent extends UIComponentBase
{

	public String getFamily()
	{
		return null;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "simple-box", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-11", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-12", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-13", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-21", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-22", null);
	
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();

		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-23", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-31", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-32", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "simple-box-33", null);
		writer.startElement("div", this);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
		
	}

}
