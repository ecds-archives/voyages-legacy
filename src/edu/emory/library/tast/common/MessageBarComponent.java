package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

public class MessageBarComponent extends UIComponentBase
{
	
	private String message;
	private boolean messageSet;
	
	public String getFamily()
	{
		return null;
	}
	
	public boolean getRendersChildren()
	{
		return true;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = message;
		return values;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		message = (String) values[1];
	}
	
	public void decode(FacesContext context)
	{
		Map params = context.getExternalContext().getRequestParameterMap(); 
		if (params.containsKey(getClientId(context)))
			setRendered(false);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		
		String message = getMessage();
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "message-bar", null);

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "message-bar", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "message-bar-text", null);
		writer.write(message);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "message-bar-button", null);
		writer.startElement("input", this);
		writer.writeAttribute("name", getClientId(context), null);
		writer.writeAttribute("type", "submit", null);
		writer.writeAttribute("value", "Hide link", null);
		writer.endElement("input");
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

	public String getMessage()
	{
		if (messageSet) return message;
		ValueBinding vb = getValueBinding("message");
		if (vb == null) return message;
		return (String) vb.getValue(getFacesContext());
	}

	public void setMessage(String message)
	{
		messageSet = true;
		this.message = message;
	}

}
