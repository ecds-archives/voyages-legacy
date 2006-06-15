package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

public class HistoryListComponent extends UIComponentBase
{
	
	private History history;
	private MethodBinding ondelete;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, ondelete);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		ondelete = (MethodBinding) restoreAttachedState(context, values[1]);
	}
	
	private String getToDeleteHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_delete";
	}
	
	public void decode(FacesContext context)
	{

		ExternalContext externalContex = context.getExternalContext();

		String toDeleteId =
			(String) externalContex.getRequestParameterMap().get(
					getToDeleteHiddenFieldName(context));

		if (toDeleteId != null && toDeleteId.length() != 0)
			queueEvent(new HistoryItemDeleteEvent(this, toDeleteId));

//		for (Iterator paramIter = context.getExternalContext().getRequestParameterNames(); paramIter.hasNext();)
//		{
//			String paramName = (String) paramIter.next();
//			String deleteId = decodeDeleteId(paramName, context);
//			if (deleteId != null)
//			{
//				queueEvent(new HistoryItemDeleteEvent(this, deleteId));
//				break;
//			}
//		}
		
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

		writer.startElement("input", this);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", getToDeleteHiddenFieldName(context), null);
		writer.endElement("input");
		
		writer.startElement("div", this);
		writer.write("History");
		writer.endElement("div");
		
		History history = getItems();
		if (history != null)
		{
			for (Iterator iter = history.getItems().iterator(); iter.hasNext();)
			{
				HistoryItem item = (HistoryItem) iter.next();
				encodeHistoryItem(item, writer, context, form);
			}
		}
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}
	
	private void encodeDeleteButton(String historyId, FacesContext context, UIForm form, ResponseWriter writer) throws IOException
	{
		
		StringBuffer js = new StringBuffer();
		
		js.append("document.");
		js.append("forms['").append(form.getClientId(context)).append("'].");
		js.append("elements['").append(getToDeleteHiddenFieldName(context)).append("'].value = ");
		js.append("'").append(historyId).append("';");

		js.append(" ");
		js.append("document.");
		js.append("forms['").append(form.getClientId(context)).append("'].");
		js.append("submit();");
		
		js.append(" ");
		js.append("return false;");
		
		writer.startElement("a", this);
		writer.writeAttribute("href", "#", null);
		writer.writeAttribute("onclick", js.toString(), null);
		writer.write("del");
		writer.endElement("a");
		
	}
	
	private void encodeHistoryItem(HistoryItem item, ResponseWriter writer, FacesContext context, UIForm form) throws IOException
	{
		
		if (item.getId() == null) return;
		
		encodeDeleteButton(item.getId(), context, form, writer);
		
		writer.startElement("div", this);
		for (Iterator iterQueryCondition = item.getQuery().getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			writer.write(queryCondition.getAttributeName());
			if (iterQueryCondition.hasNext())
			{
				writer.startElement("br", this);
				writer.endElement("br");
			}
		}
		writer.endElement("div");

//		writer.startElement("input", this);
//		writer.writeAttribute("type", "submit", null);
//		writer.writeAttribute("name", encodeDeleteId(item, context), null);
//		writer.writeAttribute("value", "Delete", null);
//		writer.endElement("input");
		
	}
	
//	private String encodeDeleteId(HistoryItem item, FacesContext context)
//	{
//		return getClientId(context) + "_delete_" + item.getId();
//	}
//	
//	private String decodeDeleteId(String value, FacesContext context)
//	{
//		String prefix = getClientId(context) + "_delete_";
//		if (!value.startsWith(prefix)) return null;
//		return value.substring(prefix.length());
//	}

	public History getItems()
	{
        if (history != null) return history;
        ValueBinding vb = getValueBinding("history");
        if (vb == null) return null;
        return (History) getValueBinding("history").getValue(getFacesContext());
	}

	public void setItems(History items)
	{
		this.history = items;
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