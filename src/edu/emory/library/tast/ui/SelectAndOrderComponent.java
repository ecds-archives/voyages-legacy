package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

/**
 * <p>
 * A component which allows users move items between two lists. It contains two
 * lists: available items and selected items. The available items list holds the
 * non-selected itema and the selected items list contains the selected items.
 * When an item is moves from either list to the other, it is removed from the
 * original list. These two collections and bindable to a bean.
 * </p>
 * <p>
 * Each item in either list can be equiped by an integer designating its order
 * with respect to the other items. The available items list always presents the
 * items in this order. In the component in marked as non-sortable (via
 * {@link #setSortable(boolean)}), the selected items list also preserves the
 * order of items. If the component is not marked as sortable, the selected
 * items can be sorted by user.
 * </p>
 * <p>
 * Moving elements between two two list and sorting is implemented via
 * JavaScript. The component uses two hidden fields to store the IDs of items in
 * the two lists and the items themselves are stored in the internal state of
 * the component (i.e. via {@link #saveState(FacesContext)} and
 * {@link #restoreState(FacesContext, Object)}).
 * </p>
 * <p>
 * The JavaScript does not hijack the onsubmit event of the main form in order
 * to create the two lists of IDs. Instead it recreates the lists every time the
 * user changes anything. This seems to work work well for hundrets of items. So
 * far it has not been tested for more. But it should be OK.
 * </p>
 * <p>
 * The component requires select-and-order.js library. The library itself
 * creates just one global JavaScript object called SelectAndOrder.
 * </p>
 * 
 * @author Jan Zich
 * 
 */
public class SelectAndOrderComponent extends UIComponentBase
{
	
	private List availableItems;
	private List selectedItems;
	private boolean availableItemsSet = false;
	private boolean selectedItemsSet = false;
	private boolean sortable = true;
	private boolean sortableSet = false;

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
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, getAvailableItems());
		values[2] = saveAttachedState(context, getSelectedItems());
		values[3] = new Boolean(sortable); 
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		availableItems = (List) restoreAttachedState(context, values[1]);
		selectedItems = (List) restoreAttachedState(context, values[2]);
		sortable = ((Boolean) values[3]).booleanValue();
	}
	
	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		String[] availableValues = ((String)params.get(getAvailableHiddenFieldName(context))).split(",");
		String[] selectedValues = ((String)params.get(getSelectedHiddenFieldName(context))).split(",");
		
		if (availableItems == null) availableItems = new ArrayList();
		if (selectedItems == null) selectedItems = new ArrayList();

		Map allItems = new HashMap();
		for (Iterator iter = availableItems.iterator(); iter.hasNext();)
		{
			SelectItem item = (SelectItem) iter.next();
			allItems.put(item.getValue(), item);
		}
		for (Iterator iter = selectedItems.iterator(); iter.hasNext();)
		{
			SelectItem item = (SelectItem) iter.next();
			allItems.put(item.getValue(), item);
		}
		
		availableItems.clear();
		for (int i = 0; i < availableValues.length; i++)
		{
			SelectItem item = (SelectItem) allItems.get(availableValues[i]);
			if (item != null) availableItems.add(item);
		}

		selectedItems.clear();
		for (int i = 0; i < selectedValues.length; i++)
		{
			SelectItem item = (SelectItem) allItems.get(selectedValues[i]);
			if (item != null) selectedItems.add(item);
		}

	}
	
	public void processUpdates(FacesContext context)
	{

		ValueBinding vbAvailableItems = getValueBinding("availableItems");
		if (vbAvailableItems != null) vbAvailableItems.setValue(context, availableItems);
	
		ValueBinding vbSelectedItems = getValueBinding("selectedItems");
		if (vbSelectedItems != null) vbSelectedItems.setValue(context, selectedItems);

	}
	
	private String getAvailableHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_available_values";
	}

	private String getSelectedHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_selected_values";
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		String clientId = getClientId(context);
		String formName = form.getClientId(context);
		boolean sortable = isSortable();
		
		List availableItems = getAvailableItems();
		List selectedItems = getSelectedItems();

		StringBuffer availableValues = new StringBuffer();
		if (availableItems != null)
		{
			for (Iterator iter = availableItems.iterator(); iter.hasNext();)
			{
				SelectItem item = (SelectItem) iter.next();
				availableValues.append(item.getValue());
				if (iter.hasNext()) availableValues.append(",");
			}
		}
		
		StringBuffer selectedValues = new StringBuffer();
		if (selectedItems != null)
		{
			for (Iterator iter = selectedItems.iterator(); iter.hasNext();)
			{
				SelectItem item = (SelectItem) iter.next();
				selectedValues.append(item.getValue());
				if (iter.hasNext()) selectedValues.append(",");
			}
		}

		JsfUtils.encodeHiddenInput(this, writer,
				getAvailableHiddenFieldName(context),
				availableValues.toString());

		JsfUtils.encodeHiddenInput(this, writer,
				getSelectedHiddenFieldName(context),
				selectedValues.toString());
		
		String availableSelectName = clientId + "_available";
		String selectedSelectName = clientId + "_selected";
		
		String jsAddFunction =
			"SelectAndOrder.add(" +
			"'" + formName + "', " +
			"'" + clientId + "', " +
			"" + (sortable ? "true" : "false") + ")";
		
		String jsRemoveFunction =
			"SelectAndOrder.remove(" +
			"'" + formName + "', " +
			"'" + clientId + "')";
		
		String jsMoveUpFunction =
			"SelectAndOrder.moveUp(" +
			"'" + formName + "', " +
			"'" + clientId + "')";
		
		String jsMoveDownFunction =
			"SelectAndOrder.moveDown(" +
			"'" + formName + "', " +
			"'" + clientId + "')";

//		String jsStoreFunctionName = "selectAndOrderStore_" + getClientId(context);
//		UtilsJSF.encodeJavaScriptStart(this, writer);
//		writer.write("function " + jsStoreFunctionName + "()\n");
//		writer.write("{\n");
//		writer.write("selectAndOrderStore('" + getClientId(context) + "');\n");
//		writer.write("}");
//		UtilsJSF.encodeJavaScriptEnd(this, writer);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.startElement("select", this);
		writer.writeAttribute("name", availableSelectName, null);
		writer.writeAttribute("multiple", "multiple", null);
		writer.writeAttribute("size", "15", null);
		writer.writeAttribute("ondblclick", jsAddFunction, null);
		if (availableItems != null)
		{
			for (Iterator iter = availableItems.iterator(); iter.hasNext();)
			{
				SelectItem item = (SelectItem) iter.next();
				writer.startElement("option", this);
				writer.writeAttribute("value", item.getOrderNumber() + ":" + item.getValue(), null);
				writer.write(item.getText());
				writer.endElement("option");
			}
		}
		writer.endElement("select");
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "select-and-order-add-remove", null);
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "select-and-order-add", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", ">", null);
		writer.writeAttribute("onclick", jsAddFunction, null);
		writer.endElement("input");
		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "select-and-order-remove", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", "<", null);
		writer.writeAttribute("onclick", jsRemoveFunction, null);
		writer.endElement("input");
		writer.endElement("div");

		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("select", this);
		writer.writeAttribute("multiple", "multiple", null);
		writer.writeAttribute("name", selectedSelectName, null);
		writer.writeAttribute("size", "15", null);
		writer.writeAttribute("ondblclick", jsRemoveFunction, null);
		if (selectedItems != null)
		{
			for (Iterator iter = selectedItems.iterator(); iter.hasNext();)
			{
				SelectItem item = (SelectItem) iter.next();
				writer.startElement("option", this);
				writer.writeAttribute("value", item.getOrderNumber() + ":" + item.getValue(), null);
				writer.write(item.getText());
				writer.endElement("option");
			}
		}
		writer.endElement("select");
		writer.endElement("td");

		if (sortable)
		{
		
			writer.startElement("td", this);
			
			writer.startElement("div", this);
			writer.startElement("input", this);
			writer.writeAttribute("type", "button", null);
			writer.writeAttribute("value", "Move up", null);
			writer.writeAttribute("onclick", jsMoveUpFunction, null);
			writer.endElement("input");
			writer.endElement("div");
			
			writer.startElement("div", this);
			writer.startElement("input", this);
			writer.writeAttribute("type", "button", null);
			writer.writeAttribute("value", "Move down", null);
			writer.writeAttribute("onclick", jsMoveDownFunction, null);
			writer.endElement("input");
			writer.endElement("div");
	
			writer.endElement("td");
		
		}
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}
	
	public List getAvailableItems()
	{
		if (availableItemsSet) return availableItems;
		ValueBinding vb = getValueBinding("availableItems");
		if (vb == null) return availableItems;
		return (List) vb.getValue(getFacesContext());
	}

	public void setAvailableItems(List items)
	{
		availableItemsSet = true;
		this.availableItems = items;
	}

	public List getSelectedItems()
	{
		if (selectedItemsSet) return selectedItems;
		ValueBinding vb = getValueBinding("selectedItems");
		if (vb == null) return selectedItems;
		return (List) vb.getValue(getFacesContext());
	}

	public void setSelectedItems(List items)
	{
		selectedItemsSet = true;
		this.selectedItems = items;
	}

	public boolean isSortable()
	{
		if (sortableSet) return sortable;
		ValueBinding vb = getValueBinding("sortable");
		if (vb == null) return sortable;
		return ((Boolean) vb.getValue(getFacesContext())).booleanValue();
	}

	public void setSortable(boolean sortable)
	{
		sortableSet = true;
		this.sortable = sortable;
	}

}
