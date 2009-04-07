package edu.emory.library.tast.common;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class InfoBoxComponent extends UIComponentBase
{

	public String getFamily()
	{
		return null;
	}
	
	public boolean getRendersChildren()
	{
		return true;
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("table", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "info-box", null);
		
		writer.startElement("tr", null);
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-top-left", null);
		writer.endElement("td");
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-top-middle", null);
		writer.endElement("td");

		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-top-right", null);
		writer.endElement("td");

		writer.endElement("tr");
		
		writer.startElement("tr", null);
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-middle-left", null);
		writer.startElement("div", null);
		writer.writeAttribute("class", "info-box-middle-left", null);
		writer.endElement("div");
		writer.endElement("td");
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-middle-middle", null);
		
	}

	public void encodeChildren(FacesContext context) throws IOException
	{
		JsfUtils.renderChildren(context, this);
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();

		writer.endElement("td");

		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-middle-right", null);
		writer.startElement("div", null);
		writer.writeAttribute("class", "info-box-middle-right", null);
		writer.endElement("div");
		writer.endElement("td");

		writer.endElement("tr");
		
		writer.startElement("tr", null);
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-bottom-left", null);
		writer.endElement("td");
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-bottom-middle", null);
		writer.endElement("td");

		writer.startElement("td", null);
		writer.writeAttribute("class", "info-box-bottom-right", null);
		writer.endElement("td");

		writer.endElement("tr");
		
		writer.endElement("table");
		
	}

}
