package edu.emory.library.tast.database.query;

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

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.database.query.searchables.ListItemsSource;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

/**
 * <p>
 * This components displays the history list in the search UI. It is bind to the
 * bean {@link edu.emory.library.tast.database.query.SearchBean} via the history list
 * represented by {@link edu.emory.library.tast.database.query.History}. It is not
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
	
	private static final int MAX_LENGTH = 20;
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
		writer.writeAttribute("src", "../images/" + imgSrc, null);
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
	 * @param session 
	 * @throws IOException
	 */
	private void encodeHistoryItem(HistoryItem item, ResponseWriter writer, FacesContext context, UIForm form, Session session) throws IOException
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
		
		QueryBuilderQuery builderQuery = item.getQuery().getBuilderQuery();
		
		writer.write(TastResource.getText("components_search_timeframe") + " ");
		writer.startElement("b", this);
		writer.write(String.valueOf(item.getQuery().getYearFrom()));
		writer.endElement("b");
		writer.write(" - ");
		writer.startElement("b", this);
		writer.write(String.valueOf(item.getQuery().getYearTo()));
		writer.endElement("b");
		
		writer.startElement("br", this);
		writer.endElement("br");
		int id = 0;
		for (Iterator iterQueryCondition = builderQuery.getConditions().iterator(); iterQueryCondition.hasNext();)
		{
			id++;
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition.next();
			writer.write(queryCondition.getSearchableAttribute().getUserLabel());
			
			if (queryCondition instanceof QueryConditionBoolean)
			{
				QueryConditionBoolean queryConditionBoolean = (QueryConditionBoolean) queryCondition;
				writer.write(": ");
				writer.startElement("b", this);
				if (queryConditionBoolean.isChecked())
				{
					writer.write(TastResource.getText("components_search_yes"));
				}
				else
				{
					writer.write(TastResource.getText("components_search_no"));
				}
				writer.endElement("b");
			}
			
			else if (queryCondition instanceof QueryConditionText)
			{
				QueryConditionText queryConditionText = (QueryConditionText) queryCondition;
				writer.write(" " + TastResource.getText("components_search_is") + " ");
				if (queryConditionText.isNonEmpty())
				{
					String toolTip = null;
					String text = queryConditionText.getValue();
					if (text.length() > MAX_LENGTH) {
						toolTip = text;
						text = text.substring(0, MAX_LENGTH) + " ... ";						
					}
					writer.startElement("b", this);
					if (toolTip != null) {
						writeToolTip(writer, this.getId() + "_tooltip_" + id + "_" + item.getId(), toolTip);
						writer.startElement("div", this);
						writer.writeAttribute("id", this.getId() + "_divquery_" + id + "_" + item.getId(), null);
						writer.writeAttribute("onmouseover", "showToolTipOff('" + this.getId() + "_tooltip_" + id + "_" + item.getId() + "', " + "'" + 
								this.getId() + "_divquery_" + id + "_" + item.getId() + "',450)", null);
						writer.writeAttribute("onmouseout", "hideToolTip('" + this.getId() + "_tooltip_" + id + "_" + item.getId() + "')", null);
						writer.write(text);
						writer.endElement("div");
					} else {
						writer.write(text);
					}					
					writer.endElement("b");
				}
				else
				{
					writer.startElement("i", this);
					writer.write(TastResource.getText("components_search_anything"));
					writer.endElement("i");
				}
			}
			
			else if (queryCondition instanceof QueryConditionRange)
			{
				QueryConditionRange queryConditionRange = (QueryConditionRange) queryCondition;
				switch (queryConditionRange.getType())
				{
					case QueryConditionRange.TYPE_BETWEEN:
						writer.write(" " + TastResource.getText("components_search_between") + " ");
						writer.startElement("b", this);
						writer.write(queryConditionRange.getFromForDisplay());
						writer.endElement("b");
						writer.write(" " + TastResource.getText("components_search_and") + " ");
						writer.startElement("b", this);
						writer.write(queryConditionRange.getToForDisplay());
						writer.endElement("b");
						break;
					
					case QueryConditionRange.TYPE_LE:
						writer.write(" " + TastResource.getText("components_search_isatmost") + " ");
						writer.startElement("b", this);
						writer.write(queryConditionRange.getLeForDisplay());
						writer.endElement("b");
						break;
						
					case QueryConditionRange.TYPE_GE:
						writer.write(" " + TastResource.getText("components_search_isatleast") + " ");
						writer.startElement("b", this);
						writer.write(queryConditionRange.getGeForDisplay());
						writer.endElement("b");
						break;

					case QueryConditionRange.TYPE_EQ:
						writer.write(" " + TastResource.getText("components_search_isequal") + " ");
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
				writer.write(" " + TastResource.getText("components_search_isequal") + " ");
				if (queryConditionList.getSelectedIds().size() > 0)
				{
					StringBuffer buffer = new StringBuffer();
					for (Iterator iter = queryConditionList.getSelectedIds().iterator(); iter.hasNext();)
					{
						QueryConditionListItem listItem = itemsSource.getItemByFullId(session, (String) iter.next());
						if (listItem != null)
						{
							buffer.append("<b>");
							buffer.append(listItem.getText());
							buffer.append("</b>");
							if (iter.hasNext()) buffer.append(" " + TastResource.getText("components_search_or") + " ");
						}
					}
					String toolTip = null;
					String text = buffer.toString();
					if (text.length() > MAX_LENGTH) {
						toolTip = text;
						int index = text.indexOf("</b>", MAX_LENGTH);
						if (index < 0) {
							index = MAX_LENGTH;
						} else {
							index += "</b>".length();
						}
						text = text.substring(0, index) + " ... ";						
						writeToolTip(writer, this.getId() + "_tooltip_" + id + "_" + item.getId(), toolTip);
						writer.startElement("div", this);
						writer.writeAttribute("id", this.getId() + "_divquery_" + id + "_" + item.getId(), null);
						writer.writeAttribute("onmouseover", "showToolTipOff('" + this.getId() + "_tooltip_" + id + "_" + item.getId() + "', " + "'" + 
								this.getId() + "_divquery_" + id + "_" + item.getId() + "',450)", null);
						writer.writeAttribute("onmouseout", "hideToolTip('" + this.getId() + "_tooltip_" + id + "_" + item.getId() + "')", null);
						writer.write(text);
						writer.endElement("div");
					} else {
						writer.write(text);
					}			
				}
				else
				{
					writer.startElement("i", this);
					writer.write(TastResource.getText("components_search_anything"));
					writer.endElement("i");
				}
			}
			
			if (iterQueryCondition.hasNext())
			{
				writer.startElement("br", this);
				writer.endElement("br");
			}
		}
		
		encodeIconsStart(writer);
		encodeIcon(writer, "icon-remove.png", jsToDelete, TastResource.getText("components_search_delete"));
		encodeIconSeparator(writer);
		encodeIcon(writer, "icon-restore.png", jsToRestore, TastResource.getText("components_search_restore"));
		encodeIconSeparator(writer);
		encodeIcon(writer, "icon-permlink.png", jsToPermlink, TastResource.getText("components_search_permlink"));
		encodeIconsEnd(writer);
		
		writer.endElement("div");
		
	}
	
	public void writeToolTip(ResponseWriter writer, String id, String text) throws IOException {
		writer.startElement("div", this);
		writer.writeAttribute("id", id, null);
		writer.writeAttribute("class", "grid-tooltip", null);
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-11l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-12l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-13l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");								
		writer.endElement("tr");
		
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-21l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-22l", null);								
		writer.startElement("div", this);
		writer.write(text);
		writer.endElement("div");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-23l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");								
		writer.endElement("tr");
		
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-31l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-32l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");
		writer.startElement("td", this);
		writer.writeAttribute("class", "bubble-33l", null);
		writer.startElement("div", this);writer.endElement("td");
		writer.endElement("td");								
		writer.endElement("tr");
		writer.endElement("table");
		
		writer.endElement("div");
	}

	/**
	 * Renders the history list. This method only renders three hidden fields
	 * which are used for firing onDelete, onRestore and onPermlink events from
	 * JavaScript. Each hidden field holds the ID of the history item which
	 * caused the event. Then this method gets the history list by calling
	 * {@link #getItems()}, which, unless somebody has set the history list by
	 * explicitly calling {@link #setItems(History)}, pulls it out from the
	 * search bean. Finally, this method calls
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
		
		Session session = HibernateUtil.getSession();
		Transaction trans = session.beginTransaction();
		
		History history = getItems();
		if (history != null)
		{
			for (Iterator iter = history.getItems().iterator(); iter.hasNext();)
			{
				HistoryItem item = (HistoryItem) iter.next();
				encodeHistoryItem(item, writer, context, form, session);
			}
		}
		
		trans.commit();
		session.close();

	}

	/**
	 * Gets the current history list from the linked bean, unless somebody has
	 * in this postback set the history list by explicitly calling
	 * {@link #setItems(History)}, in which case it just returns the
	 * explicitly set value.
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