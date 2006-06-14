package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

public class HistoryList extends UIComponentBase
{
	
	private List items;
	private MethodBinding ondelete;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = ondelete;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		super.restoreState(context, state);
	}
	
	public void decode(FacesContext context)
	{

		for (Iterator paramIter = context.getExternalContext().getRequestParameterNames(); paramIter.hasNext();)
		{
			String paramName = (String) paramIter.next();
			String deleteId = decodeDeleteId(paramName, context);
			if (deleteId != null)
			{
				queueEvent(new HistoryItemDeleteEvent(this, deleteId));
				break;
			}
		}
		
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
		
		if (event instanceof HistoryItemDeleteEvent)
			if (ondelete != null)
				ondelete.invoke(getFacesContext(), new Object[] {event});
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);

		// header
		writer.startElement("div", this);
		writer.write("History");
		writer.endElement("div");
		
		// retrieve the list of items from a bean
		List items = (List) getValueBinding("items").getValue(context);
		if (items == null) return;
		
		// render the list
		for (Iterator iter = items.iterator(); iter.hasNext();)
		{
			HistoryItem item = (HistoryItem) iter.next();
			encodeHistoryItem(item, writer, context, form);
		}
		
	}
	
	private void encodeHistoryItem(HistoryItem item, ResponseWriter writer, FacesContext context, UIForm form) throws IOException
	{
		
		writer.startElement("div", this);
		
		writer.write(item.getId());

		writer.startElement("input", this);
		writer.writeAttribute("type", "submit", null);
		writer.writeAttribute("name", encodeDeleteId(item, context), null);
		writer.writeAttribute("value", "Delete", null);
		writer.endElement("input");
		
		writer.endElement("div");
		
	}
	
	private String encodeDeleteId(HistoryItem item, FacesContext context)
	{
		return getClientId(context) + "_delete_" + item.getId();
	}
	
	private String decodeDeleteId(String value, FacesContext context)
	{
		String prefix = getClientId(context) + "_delete_";
		if (!value.startsWith(prefix)) return null;
		return value.substring(prefix.length());
	}

	public List getItems()
	{
        if (items != null) return items;
        ValueBinding vb = getValueBinding("items");
        return (List) (vb != null ? getValueBinding("items").getValue(getFacesContext()) : null);
	}

	public void setItems(List items)
	{
		this.items = items;
	}

	public MethodBinding getOndelete()
	{
		return ondelete;
	}

	public void setOndelete(MethodBinding ondelete)
	{
		this.ondelete = ondelete;
	}

}