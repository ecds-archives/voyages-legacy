package edu.emory.library.tast.ui.search.query;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.ui.search.query.searchables.ListItemsSource;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

/**
 * <p>
 * This components displays the history list in the search UI. It is bind to the
 * bean {@link edu.emory.library.tast.ui.search.query.SearchBean} via the history list
 * represented by {@link edu.emory.library.tast.ui.search.query.History}. It is not
 * responsible for keeping the state of history, it only renders the histoty
 * list and fires events to the bean when a history item needs to be deleted,
 * restored and references by a permlink. Thus, the bean does not have to
 * provide a setter for the history, just a getter.
 * </p>
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

	/**
	 * Required by JSF.
	 */
	public String getFamily()
	{
		return null;
	}
	
	/**
	 * Stores the event bindings to onDelete, onRestore and onPermlink along
	 * with the state of the ancestor class.
	 */
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, onDelete);
		values[2] = saveAttachedState(context, onRestore);
		values[3] = saveAttachedState(context, onPermlink);
		return values;
	}
	
	/**
	 * Restores the event bindings to onDelete, onRestore and onPermlink along
	 * with the state of the ancestor class.
	 */
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		onDelete = (MethodBinding) restoreAttachedState(context, values[1]);
		onRestore = (MethodBinding) restoreAttachedState(context, values[2]);
		onPermlink = (MethodBinding) restoreAttachedState(context, values[3]);
	}

	/**
	 * The name of the hidden field in which, when an user clicks on the delete
	 * button, a client JavaScript stores the history ID of a history item which
	 * is supposed to be deleted on server.
	 * 
	 * @param context
	 *            Needed because the field name is based on JSF client ID of
	 *            this component.
	 * @return The field name.
	 */
	private String getToDeleteHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_delete";
	}
	
	/**
	 * The name of the hidden field in which, when an user clicks on the restore
	 * button, a client JavaScript stores the history ID of a history item which
	 * is supposed to be restored from the history on server.
	 * 
	 * @param context
	 *            Needed because the field name is based on JSF client ID of
	 *            this component.
	 * @return The field name.
	 */
	private String getToRestoreHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_restore";
	}

	/**
	 * The name of the hidden field in which, when an user clicks on the
	 * permlink button, a client JavaScript stores the history ID of a history
	 * item on which a permlink is supposed to be created on server.
	 * 
	 * @param context
	 *            Needed because the field name is based on JSF client ID of
	 *            this component.
	 * @return The field name.
	 */
	private String getToPermlinkHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_to_permlink";
	}

	/**
	 * Inspects the three hidden fields (for onDelete, onRestore and onPermlink)
	 * in order to determine if and eventually which event was fired, and then
	 * creates the event objects and saves it to the JSF event queue.
	 */
	public void decode(FacesContext context)
	{

		Map params = context.getExternalContext().getRequestParameterMap();

		String toDeleteId = (String) params.get(getToDeleteHiddenFieldName(context));
		if (toDeleteId != null && toDeleteId.length() != 0)
			queueEvent(new HistoryItemDeleteEvent(this, toDeleteId));

		String toRestoreId = (String) params.get(getToRestoreHiddenFieldName(context));
		if (toRestoreId != null && toRestoreId.length() != 0)
			queueEvent(new HistoryItemRestoreEvent(this, toRestoreId));

		String toCreatePermlinkId = (String) params.get(getToPermlinkHiddenFieldName(context));
		if (toCreatePermlinkId != null && toCreatePermlinkId.length() != 0)
			queueEvent(new HistoryItemPermlinkEvent(this, toCreatePermlinkId));

	}
	
	/**
	 * Fires the quened events to the linked bean which were previously stored
	 * in {@link #decode(FacesContext)}.
	 */
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
	
	/**
	 * Starts a block of item buttons.
	 * 
	 * @param writer
	 *            HTML sent to client.
	 * @throws IOException
	 */
	private void encodeIconsStart(ResponseWriter writer) throws IOException
	{
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "history-item-buttons", null);
		writer.startElement("tr", this);
	}
	
	/**
	 * Render single icon of a history item.
	 * 
	 * @param writer
	 *            HTML sent to client.
	 * @param imgSrc
	 *            Image of the icon. Has to be 12x12.
	 * @param jsOnClick
	 *            JavaScipt which fires the corresponding event.
	 * @param text
	 *            Label of the icon.
	 * @throws IOException
	 */
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
		}
	}

	/**
	 * Renders a separator between two buttons in a block of item buttons.
	 * 
	 * @param writer
	 *            HTML sent to client.
	 * @throws IOException
	 */
	private void encodeIconSeparator(ResponseWriter writer) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "history-list-icon-space", null);
		writer.endElement("td");
	}

	/**
	 * Ends a block on item buttons.
	 * 
	 * @param writer
	 *            HTML sent to client.
	 * @throws IOException
	 */
	private void encodeIconsEnd(ResponseWriter writer) throws IOException
	{
		writer.endElement("tr");
		writer.endElement("table");
	}

	/**
	 * Called by {@link #encodeBegin(FacesContext)}. Renders a representation
	 * of one history item depending on its type. This will need some clever
	 * ideas to make it usable because really long querys can take up a lot of
	 * space. It also generates the buttons and JavaScript for the three events
	 * onDelete, onRestore and onPermlink.
	 * 
	 * @param item
	 *            History item to render.
	 * @param writer
	 *            HTML sent to client.
	 * @param context
	 *            JSF context.
	 * @param form
	 *            Enclosing JSF form. Needed because the JavaScript needs to
	 *            reference the hiddem fields.
	 * @throws IOException
	 */
	private void encodeHistoryItem(HistoryItem item, ResponseWriter writer, FacesContext context, UIForm form) throws IOException
	{
		
		if (item.getId() == null) return;
		
		String jsToDelete = JsfUtils.generateSubmitJS(
				context, form,
				getToDeleteHiddenFieldName(context),
				item.getId());

		String jsToRestore = JsfUtils.generateSubmitJS(
				context, form,
				getToRestoreHiddenFieldName(context),
				item.getId());
		
		String jsToPermlink = JsfUtils.generateSubmitJS(
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
				writer.write(queryCondition.getSearchableAttribute().getUserLabel());
				
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
				
				else if (queryCondition instanceof QueryConditionRange)
				{
					QueryConditionRange queryConditionRange = (QueryConditionRange) queryCondition;
					switch (queryConditionRange.getType())
					{
						case QueryConditionRange.TYPE_BETWEEN:
							writer.write(" is between ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getFromForDisplay());
							writer.endElement("b");
							writer.write(" and ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getToForDisplay());
							writer.endElement("b");
							break;
						
						case QueryConditionRange.TYPE_LE:
							writer.write(" is at most ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getLeForDisplay());
							writer.endElement("b");
							break;
							
						case QueryConditionRange.TYPE_GE:
							writer.write(" is at least ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getGeForDisplay());
							writer.endElement("b");
							break;
	
						case QueryConditionRange.TYPE_EQ:
							writer.write(" is equal ");
							writer.startElement("b", this);
							writer.write(queryConditionRange.getEqForDisplay());
							writer.endElement("b");
							break;
					}
					
				}
				
				else if (queryCondition instanceof QueryConditionList)
				{
					QueryConditionList queryConditionList = (QueryConditionList) queryCondition;
					ListItemsSource itemsSource = (ListItemsSource) queryConditionList.getSearchableAttribute();
					writer.write(" is ");
					if (queryConditionList.getSelectedIds().size() > 0)
					{
						for (Iterator iter = queryConditionList.getSelectedIds().iterator(); iter.hasNext();)
						{
							QueryConditionListItem listItem = itemsSource.getItemByFullId((String) iter.next());
							writer.startElement("b", this);
							writer.write(listItem.getText());
							writer.endElement("b");
							if (iter.hasNext()) writer.write(" or ");
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
		
		encodeIconsStart(writer);
		encodeIcon(writer, "icon-remove.png", jsToDelete, "delete");
		encodeIconSeparator(writer);
		encodeIcon(writer, "icon-restore.png", jsToRestore, "restore");
		encodeIconSeparator(writer);
		encodeIcon(writer, "icon-permlink.png", jsToPermlink, "permlink");
		encodeIconsEnd(writer);
		
		writer.endElement("div");
		
	}

	/**
	 * Renders the history list. This method only renders three hidden fields
	 * which are used for firing onDelete, onRestore and onPermlink events from
	 * JavaScript. Each hidden field holds the ID of the history item which
	 * caused the event. Then this method gets the history list by calling
	 * {@link #getItems()}, which, unless somebody has set the history list by
	 * explicitelly calling {@link #setItems(History)}, pulls it out from the
	 * seach bean. Finally, this method calls
	 * {@link #encodeHistoryItem(HistoryItem, ResponseWriter, FacesContext, UIForm)}
	 * for each history item.
	 */
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		JsfUtils.encodeHiddenInput(this, writer, getToDeleteHiddenFieldName(context));
		JsfUtils.encodeHiddenInput(this, writer, getToRestoreHiddenFieldName(context));
		JsfUtils.encodeHiddenInput(this, writer, getToPermlinkHiddenFieldName(context));
		
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

	/**
	 * Gets the current history list from the linked bean, unless somebody has
	 * in this postback set the history list by explicitelly calling
	 * {@link #setItems(History)}, in which case it just returns the
	 * explicitelly set value.
	 * 
	 * @return The current history list.
	 */
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