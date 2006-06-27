package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Attribute;

public class VoyageDetailComponent extends UIComponentBase
{
	
	private Long voyageId;
	private boolean voyageIdSet = false;

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

		writer.startElement("table", this);
		writer.writeAttribute("border", "1", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		Long voyageId = getVoyageId();
		if (voyageId == null) return;
		
		List voyages = Voyage.loadAllRevisions(voyageId, 0);
		Attribute[] attributes = Voyage.getAttributes();

		writer.startElement("th", this);
		writer.endElement("th");

		writer.startElement("tr", this);
		for (Iterator iter = voyages.iterator(); iter.hasNext();)
		{
			writer.startElement("th", this);
			Voyage voyage = (Voyage) iter.next();
			writer.write(voyage.getVoyageId().toString());
			writer.endElement("th");
		}
		writer.endElement("tr");

		for (int i = 0; i < attributes.length; i++)
		{
			Attribute attr = attributes[i];
			
			writer.startElement("tr", this);
			writer.startElement("td", this);
			writer.write(attr.getUserLabelOrName());
			writer.endElement("td");
			
			for (Iterator iter = voyages.iterator(); iter.hasNext();)
			{
				writer.startElement("td", this);
				Voyage voyage = (Voyage) iter.next();
				writer.write(voyage.getAttrValue(attr.getName()).toString());
				writer.endElement("td");
			}
			
			writer.endElement("tr");
		
		}
		
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public Long getVoyageId()
	{
		if (voyageIdSet) return voyageId;
		ValueBinding vb = getValueBinding("voyageId");
		if (vb == null) return voyageId;
		return (Long) vb.getValue(getFacesContext());
	}

	public void setVoyageId(Long voyageId)
	{
		voyageIdSet = true;
		this.voyageId = voyageId;
	}

}
