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

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.util.StringUtils;

/**
 * 
 * @author Jan Zich
 *
 */
public class HistoryListComponent extends UIComponentBase
{
	
	private History history;
	private MethodBinding onDelete;
	private MethodBinding onRestore;
	private MethodBinding onPermlink;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, onDelete);
		values[2] = saveAttachedState(context, onRestore);
		values[3] = saveAttachedState(context, onPermlink);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		onDelete = (MethodBinding) restoreAttachedState(context, values[1]);
		onRestore = (MethodBinding) restoreAttachedState(context, values[2]);
		onPermlink = (MethodBinding) restoreAttachedState(context, values[3]);
	}
	
	private String getToDeleteHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_delete";
	}
	
	private String getToRestoreHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_restore";
	}

	private String getToPermlinkHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_permlink";
	}

	public void decode(FacesContext context)
	{

		ExternalContext externalContex = context.getExternalContext();

		String toDeleteId = (String) externalContex.getRequestParameterMap().get(
				getToDeleteHiddenFieldName(context));

		if (toDeleteId != null && toDeleteId.length() != 0)
			queueEvent(new HistoryItemDeleteEvent(this, toDeleteId));

		String toRestoreId = (String) externalContex.getRequestParameterMap().get(
				getToRestoreHiddenFieldName(context));

		if (toRestoreId != null && toRestoreId.length() != 0)
			queueEvent(new HistoryItemRestoreEvent(this, toRestoreId));

		String toCreatePermlinkId = (String) externalContex.getRequestParameterMap().get(
				getToPermlinkHiddenFieldName(context));

		if (toCreatePermlinkId != null && toCreatePermlinkId.length() != 0)
			queueEvent(new HistoryItemPermlinkEvent(this, toCreatePermlinkId));

	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
		
		if (event instanceof HistoryItemDeleteEvent)
			if (onDelete != null)
				onDelete.invoke(getFacesContext(), new Object[] {event});
		
		if (event instanceof HistoryItemRestoreEvent)
			if (onRestore != null)
				onRestore.invoke(getFacesContext(), new Object[] {event});

		if (event instanceof HistoryItemPermlinkEvent)
			if (onPermlink != null)
				onPermlink.invoke(getFacesContext(), new Object[] {event});

	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		UtilsJSF.encodeHiddenInput(this, writer, getToDeleteHiddenFieldName(context), null);
		UtilsJSF.encodeHiddenInput(this, writer, getToRestoreHiddenFieldName(context), null);
		UtilsJSF.encodeHiddenInput(this, writer, getToPermlinkHiddenFieldName(context), null);
		
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
	
	private void encodeIcon(ResponseWriter writer, String imgSrc, String jsOnClick, String text) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "history-list-icon", null);
		writer.writeAttribute("onclick", jsOnClick, null);
		writer.startElement("img", this);
		writer.writeAttribute("src", imgSrc, null);
		writer.writeAttribute("class", "history-list-icon", null);
		writer.writeAttribute("width", "12", null);
		writer.writeAttribute("height", "12", null);
		writer.writeAttribute("border", "0", null);
		writer.endElement("img");
		writer.endElement("td");
		
		if (!StringUtils.isNullOrEmpty(text))
		{
			writer.startElement("td", this);
			writer.writeAttribute("class", "history-list-icon-text", null);
			writer.writeAttribute("onclick", jsOnClick, null);
			writer.write(text);
			writer.endElement("td");
			writer.startElement("td", this);
			writer.writeAttribute("class", "history-list-icon-space", null);
			writer.endElement("td");
		}
	}
	
	private void encodeHistoryItem(HistoryItem item, ResponseWriter writer, FacesContext context, UIForm form) throws IOException
	{
		
		if (item.getId() == null) return;
		
		String jsToDelete = UtilsJSF.generateSubmitJS(
				context, form,
				getToDeleteHiddenFieldName(context),
				item.getId());

		String jsToRestore = UtilsJSF.generateSubmitJS(
				context, form,
				getToRestoreHiddenFieldName(context),
				item.getId());
		
		String jsToPermlink = UtilsJSF.generateSubmitJS(
				context, form,
				getToPermlinkHiddenFieldName(context),
				item.getId());

		writer.startElement("div", this);
		writer.writeAttribute("class", "side-box", null);
		
		if (item.getQuery().getConditionCount() == 0)
		{
			writer.write("No conditions. This query returns everything.");
		}
		else
		{
			
			for (Iterator iterQueryCondition = item.getQuery().getConditions().iterator(); iterQueryCondition.hasNext();)
			{
				QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
				writer.write(queryCondition.getAttribute().getUserLabel());
				
				if (queryCondition instanceof QueryConditionText)
				{
					QueryConditionText queryConditionText = (QueryConditionText) queryCondition;
					writer.write(" is ");
					if (queryConditionText.isNonEmpty())
					{
						writer.startElement("b", this);
						writer.write(queryConditionText.getValue());
						writer.endElement("b");
					}
					else
					{
						writer.startElement("i", this);
						writer.write("[anything]");
						writer.endElement("i");
					}
				}
				
				else if (queryCondition instanceof QueryConditionNumeric)
				{
					QueryConditionNumeric queryConditionRange = (QueryConditionNumeric) queryCondition;
					switch (queryConditionRange.getType())
					{
						case QueryConditionNumeric.TYPE_BETWEEN:
							writer.write(" is between ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getFrom());
							writer.endElement("b");
							writer.write(" and ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getTo());
							writer.endElement("b");
							break;
						
						case QueryConditionNumeric.TYPE_LE:
							writer.write(" is at most ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getLe());
							writer.endElement("b");
							break;
							
						case QueryConditionNumeric.TYPE_GE:
							writer.write(" is at least ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getGe());
							writer.endElement("b");
							break;
	
						case QueryConditionNumeric.TYPE_EQ:
							writer.write(" is equal ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getEq());
							writer.endElement("b");
							break;
					}
				}
				
				else if (queryCondition instanceof QueryConditionDictionary)
				{
					QueryConditionDictionary queryConditionList = (QueryConditionDictionary) queryCondition;
					writer.write(" is ");
					if (queryConditionList.getDictionaries().size() > 0)
					{
						for (Iterator iterDict = queryConditionList.getDictionaries().iterator(); iterDict.hasNext();)
						{
							Dictionary dict = (Dictionary) iterDict.next();
							writer.startElement("b", this);
							writer.write(dict.getName());
							writer.endElement("b");
							if (iterDict.hasNext()) writer.write(" or ");
						}
					}
					else
					{
						writer.startElement("i", this);
						writer.write("[anything]");
						writer.endElement("i");
					}
				}
				
				if (iterQueryCondition.hasNext())
				{
					writer.startElement("br", this);
					writer.endElement("br");
				}
				
			}
		}
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "history-item-buttons", null);
		writer.startElement("tr", this);
		
//		writer.startElement("td", this);
//		writer.writeAttribute("class", "side-box-button", null);
//		writer.writeAttribute("onclick", jsToDelete, null);
//		writer.write("&times;");
//		writer.endElement("td");
		encodeIcon(writer, "icon-remove.png", jsToDelete, "delete");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "side-box-button-space", null);
		writer.endElement("td");

//		writer.startElement("td", this);
//		writer.writeAttribute("class", "side-box-button", null);
//		writer.writeAttribute("onclick", jsToRestore, null);
//		writer.write("restore");
//		writer.endElement("td");
		encodeIcon(writer, "icon-restore.png", jsToRestore, "restore");

		writer.startElement("td", this);
		writer.writeAttribute("class", "side-box-button-space", null);
		writer.endElement("td");

//		writer.startElement("td", this);
//		writer.writeAttribute("class", "side-box-button", null);
//		writer.writeAttribute("onclick", jsToPermlink, null);
//		writer.write("permlink");
//		writer.endElement("td");
		encodeIcon(writer, "icon-permlink.png", jsToPermlink, "permlink");

		writer.endElement("tr");
		writer.endElement("table");
		
		writer.endElement("div");
		
	}
	
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

	public MethodBinding getOnDelete()
	{
		return onDelete;
	}

	public void setOnDelete(MethodBinding ondelete)
	{
		this.onDelete = ondelete;
	}

	public MethodBinding getOnRestore()
	{
		return onRestore;
	}

	public void setOnRestore(MethodBinding onrestore)
	{
		this.onRestore = onrestore;
	}

	public MethodBinding getOnPermlink()
	{
		return onPermlink;
	}

	public void setOnPermlink(MethodBinding onPermlink)
	{
		this.onPermlink = onPermlink;
	}

}