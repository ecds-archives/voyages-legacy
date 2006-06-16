package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class DictionaryListComponent extends UIComponentBase
{
	
	private String attribute;
	private boolean attributeSet = false;
	private String formName;
	private Dictionary[] items;
	private boolean itemsSet = false;
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = attribute;
		values[2] = formName;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		attribute = (String) values[1];
		formName = (String) values[2];
	}
	
	public void decode(FacesContext context)
	{
		Map params = context.getExternalContext().getRequestParameterMap();
		if (params.containsKey("attributeName") && params.containsKey("formName"))
		{
			attribute = (String) params.get("attributeName");
			formName = (String) params.get("formName");
		}
	}

	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("attribute");
		if (vb != null) vb.setValue(context, attribute);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();

		String hiddenFieldName = null;
		String displayFieldName = null;
		
		Map params = context.getExternalContext().getRequestParameterMap();
		if (params.containsKey("attributeName") && params.containsKey("formName"))
		{
			attribute = (String) params.get("attributeName");
			formName = (String) params.get("formName");
			hiddenFieldName = (String) params.get("hiddenFieldName");
			displayFieldName = (String) params.get("displayFieldName");
		}

		UtilsJSF.encodeJavaScriptStart(this, writer);
		writer.write("var formName = '" + formName + "';");
		writer.write("var attributeName = '" + attribute + "';");
		writer.write("var hiddenFieldName = '" + hiddenFieldName + "';");
		writer.write("var displayFieldName = '" + displayFieldName + "';");
		UtilsJSF.encodeJavaScriptEnd(this, writer);

		SchemaColumn col = Voyage.getSchemaColumn(attribute);
		Dictionary items[] = Dictionary.loadDictionary(col.getDictinaory());
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);

		if (items != null)
		{
			for (int i = 0; i < items.length; i++)
			{
				Dictionary item = items[i];
				writer.startElement("tr", this);
	
				writer.startElement("td", this);
				writer.startElement("input", this);
				writer.writeAttribute("value", item.getId(), null);
				writer.writeAttribute("type", "checkbox", null);
				writer.endElement("input");
				writer.endElement("td");
	
				writer.startElement("td", this);
				writer.write(item.getName());
				writer.endElement("td");
			
				writer.endElement("tr");
			}
		}
		
		writer.endElement("table");

	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public String getFamily()
	{
		return null;
	}

	public String getAttributeName()
	{
		if (attributeSet) return attribute;
		ValueBinding vb = getValueBinding("attribute");
		if (vb == null) return attribute;
		return (String) vb.getValue(getFacesContext());
	}

	public void setAttributeName(String attributeName)
	{
		attributeSet = true;
		this.attribute = attributeName;
	}

	public Dictionary[] getItems()
	{
		if (itemsSet) return items;
		ValueBinding vb = getValueBinding("items");
		if (vb == null) return items;
		return (Dictionary[]) vb.getValue(getFacesContext());
	}

	public void setItems(Dictionary[] items)
	{
		itemsSet = true;
		this.items = items;
	}

}