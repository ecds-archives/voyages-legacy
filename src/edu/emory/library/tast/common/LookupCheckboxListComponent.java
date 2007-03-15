package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class LookupCheckboxListComponent extends UIComponentBase
{
	
	public static final String ID_PARTS_SEPARATOR = ":";
	private static final String ID_SEPARATOR = ",";
	
	private boolean selectedValuesSet = false;
	private String[] selectedValues;

	private boolean expandedValuesSet = false;
	private String[] expandedValues;

	private boolean itemsSet = false;
	private LookupCheckboxItem[] items;

	private boolean onChangeSet = false;
	private String onChange;

	public String getFamily()
	{
		return null;
	}
	
	private String getHtmlNameForList(FacesContext context)
	{
		return getClientId(context);
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = onChange;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		onChange = (String) values[1];
	}
	
	private String getHtmlNameListItemElement(FacesContext context, String fullId)
	{
		return getClientId(context) + "_item_" + fullId;
	}

	private String getHtmlNameListChildElement(FacesContext context, String fullId)
	{
		return getClientId(context) + "_chld_" + fullId;
	}

	private String getHtmlNameListArrowElement(FacesContext context, String fullId)
	{
		return getClientId(context) + "_arrow_" + fullId;
	}

	private String getHtmlNameForListCheckbox(FacesContext context, String fullId)
	{
		return getClientId(context) + "_" + fullId;
	}

	private String getHtmlNameForListExpanded(FacesContext context)
	{
		return getClientId(context) + "_expanded_ids";
	}
	
	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();
		Map paramValues = context.getExternalContext().getRequestParameterValuesMap();
		
		selectedValues = (String[]) paramValues.get(getHtmlNameForList(context));
		
		String expandedIdsStr = (String) params.get(getHtmlNameForListExpanded(context));
		if (expandedIdsStr != null) expandedValues = expandedIdsStr.split(ID_SEPARATOR);
		
	}
	
	public void processUpdates(FacesContext context)
	{
		
		if (selectedValues != null)
		{
			ValueBinding vbSelectedValues = getValueBinding("selectedValues");
			if (vbSelectedValues != null) vbSelectedValues.setValue(context, selectedValues);
		}
		else
		{
			System.out.println("selectedValues = null -> not updating");
		}

		if (expandedValues != null)
		{
			ValueBinding vbExpandedValues = getValueBinding("expandedValues");
			if (vbExpandedValues != null) vbExpandedValues.setValue(context, expandedValues);
		}
		else
		{
			System.out.println("expandedValues = null -> not updating");
		}

	}

	private void createRegJsForListItem(FacesContext context, LookupCheckboxItem item, String parentId, StringBuffer regJS, Set expandedValuesLookup)
	{
		
		String fullId;
		if (StringUtils.isNullOrEmpty(parentId))
			fullId = item.getId();
		else
			fullId = parentId + ID_PARTS_SEPARATOR + item.getId();

		regJS.append("'").append(item.getId()).append("': ");
		regJS.append("{");
		regJS.append("checkboxId: '").append(getHtmlNameForListCheckbox(context, fullId)).append("'");
		regJS.append(", ");
		regJS.append("itemElementId: '").append(getHtmlNameListItemElement(context, fullId)).append("'");
		regJS.append(", ");
		regJS.append("childrenElementId: '").append(getHtmlNameListChildElement(context, fullId)).append("'");
		regJS.append(", ");
		regJS.append("arrowElementId: '").append(getHtmlNameListArrowElement(context, fullId)).append("'");
		regJS.append(", ");
		regJS.append("expanded: ").append(expandedValuesLookup.contains(fullId) ? "true" : "false");
		regJS.append(", ");
		regJS.append("text: '").append(JsfUtils.escapeStringForJS(item.getText())).append("'");
		
		if (item.hasChildren())
		{
			regJS.append(", ");
			regJS.append("children: {");
			LookupCheckboxItem[] children = item.getChildren();
			for (int i = 0; i < children.length; i++)
			{
				if (i > 0) regJS.append(", ");
				createRegJsForListItem(context, children[i], fullId, regJS, expandedValuesLookup);
			}
			regJS.append("}");
		}
		
		regJS.append("}");
		
	}
	
	private void encodeListItem(FacesContext context, ResponseWriter writer, LookupCheckboxItem item, int level, String parentId, Set selectedValuesLookup, Set expandedValuesLookup) throws IOException
	{
		
		String fullId;
		if (StringUtils.isNullOrEmpty(parentId))
			fullId = item.getId();
		else
			fullId = parentId + ID_PARTS_SEPARATOR + item.getId();
		
		boolean isExpanded =
			expandedValuesLookup.contains(fullId);
		
		String jsCheckboxClick = 
			"LookupCheckboxListGlobals.itemToggled(" +
			"'" + getClientId(context) + "'," +
			"this)";
		
		String className =
			"lookup-checkbox-list-item-" + level + 
			(item.hasChildren() ? " query-builder-list-item-with-children" : "");
		
		writer.startElement("tr", this);
		writer.writeAttribute("id", getHtmlNameListItemElement(context, fullId), null);

		writer.startElement("td", this);
		if (item.hasChildren())
		{

			String jsArrowClick = 
				"LookupCheckboxListGlobals.itemExpandCollapse(" +
				"'" + getClientId(context) + "'," +
				"'" + fullId + "'," +
				"this)";
			
			String arrowClassName = isExpanded ? 
				"lookup-checkbox-list-item-expanded" :
				"lookup-checkbox-list-item-collapsed";
			
			writer.startElement("div", this);
			writer.writeAttribute("class", arrowClassName, null);
			writer.writeAttribute("onclick", jsArrowClick, null);
			writer.writeAttribute("id", getHtmlNameListArrowElement(context, fullId), null);
			writer.endElement("div");
			
		}
		writer.endElement("td");

		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "checkbox", null);
		writer.writeAttribute("id", getHtmlNameForListCheckbox(context, fullId), null);
		writer.writeAttribute("value", fullId, null);
		writer.writeAttribute("onclick", jsCheckboxClick, null);
		writer.writeAttribute("name", getHtmlNameForList(context), null);
		if (selectedValuesLookup.contains(fullId)) writer.writeAttribute("checked", "checked", null);
		writer.endElement("input");
		writer.endElement("td");

		writer.startElement("td", this);
		writer.writeAttribute("class", className, null);
		writer.write(item.getText());
		writer.endElement("td");
	
		writer.endElement("tr");
		
		if (item.hasChildren())
		{
			LookupCheckboxItem[] children = item.getChildren();
			
			writer.startElement("tr", this);
			writer.writeAttribute("id", getHtmlNameListChildElement(context, fullId), null);
			if (!isExpanded) writer.writeAttribute("style", "display: none", null);
			
			writer.startElement("td", this);
			writer.endElement("td");

			writer.startElement("td", this);
			writer.endElement("td");
			
			writer.startElement("td", this);
			writer.startElement("table", this);
			writer.writeAttribute("cellspacing", "0", null);
			writer.writeAttribute("cellpadding", "0", null);
			writer.writeAttribute("border", "0", null);
			writer.startElement("tr", this);
			for (int i = 0; i < children.length; i++)
			{
				encodeListItem(context, writer, children[i], level+1, fullId, selectedValuesLookup, expandedValuesLookup);
			}
			writer.endElement("table");
			writer.endElement("td");

			writer.endElement("tr");
		}
		
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// get data from a bean
		items = getItems();
		selectedValues = getSelectedValues();
		expandedValues = getExpandedValues();
		onChange = getOnChange();
		
		// lookup for selected and expanded values
		Set selectedValuesLookup = StringUtils.toStringSet(selectedValues);
		Set expandedValuesLookup = StringUtils.toStringSet(expandedValues);
		
		// register JS
		StringBuffer regJS = new StringBuffer();
		regJS.append("LookupCheckboxListGlobals.registerList(new LookupCheckboxList(");
		regJS.append("'").append(getClientId(context)).append("'");
		regJS.append(", ");
		regJS.append("'").append(form.getClientId(context)).append("'");
		regJS.append(", ");
		regJS.append("'").append(getHtmlNameForListExpanded(context)).append("'");
		regJS.append(", ");
		regJS.append("'").append(ID_PARTS_SEPARATOR).append("'");
		regJS.append(", ");
		regJS.append("'").append(ID_SEPARATOR).append("'");
		regJS.append(", ");
		regJS.append("true");
		regJS.append(", ");
		regJS.append("{");
		for (int i = 0; i < items.length; i++)
		{
			if (i > 0) regJS.append(", ");
			createRegJsForListItem(context, items[i], "", regJS, expandedValuesLookup);
		}
		regJS.append("}");
		regJS.append(", ");
		if (onChange != null) regJS.append(onChange); else regJS.append("null"); 
		regJS.append("))");

		// render js
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);

		// expanded ids
		JsfUtils.encodeHiddenInput(this, writer,
				getHtmlNameForListExpanded(context),
				StringUtils.join(ID_SEPARATOR, expandedValues));

		// select all
		String jsSelectAll = 
			"LookupCheckboxListGlobals.selectAll(" +
			"'" + getClientId(context) + "')";

		// deselect all
		String jsDeselectAll = 
			"LookupCheckboxListGlobals.deselectAll(" +
			"'" + getClientId(context) + "')";
		
		// quicksearch JS
		String jsQuickSearch =
			"LookupCheckboxListGlobals.quickSearch(" + 
			"'" + getClientId(context) + "', " +
			"this)";
		
		// quicksearch table container: start
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "lookup-checkbox-list-quicksearch", null);
		writer.startElement("tr", this);
		
		// quicksearch title
		writer.startElement("td", this);
		writer.writeAttribute("class", "lookup-checkbox-list-quicksearch-label", null);
		writer.write(TastResource.getText("components_search_quicksearch"));
		writer.endElement("td");

		// quicksearch
		writer.startElement("td", this);
		writer.writeAttribute("class", "lookup-checkbox-list-quicksearch-input", null);
		writer.startElement("input", this);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("name", "", null);
		writer.writeAttribute("onkeyup", jsQuickSearch, null);
		writer.endElement("input");
		writer.endElement("td");

		// quicksearch table container: end
		writer.endElement("tr");
		writer.endElement("table");

		// main list container
		writer.startElement("div", this);
		writer.writeAttribute("class", "lookup-checkbox-list", null);

		// main list
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		
		// actual list
		for (int i = 0; i < items.length; i++)
			encodeListItem(
					context, writer,
					items[i], 0, "",
					selectedValuesLookup,
					expandedValuesLookup);
		
		// list: end
		writer.endElement("table");
		writer.endElement("div");

		// bottom buttons
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "lookup-checkbox-list-buttons", null);
		writer.startElement("tr", this);
		
		// select all
		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", TastResource.getText("components_search_selall"), null);
		writer.writeAttribute("onclick", jsSelectAll, null);
		writer.endElement("input");
		writer.endElement("td");
		
		// deselect all
		writer.startElement("td", this);
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", TastResource.getText("components_search_deselall"), null);
		writer.writeAttribute("onclick", jsDeselectAll, null);
		writer.endElement("input");
		writer.endElement("td");

		// buttons: end
		writer.endElement("tr");
		writer.endElement("table");
		
	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public LookupCheckboxItem[] getItems()
	{
		return (LookupCheckboxItem[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"items", itemsSet, items);
	}

	public void setItems(LookupCheckboxItem[] items)
	{
		itemsSet = true;
		this.items = items;
	}

	public String[] getSelectedValues()
	{
		return JsfUtils.getCompPropStringArray(this, getFacesContext(),
				"selectedValues", selectedValuesSet, selectedValues);
	}

	public void setSelectedValues(String[] selectedValues)
	{
		selectedValuesSet = true;
		this.selectedValues = selectedValues;
	}

	public String[] getExpandedValues()
	{
		return JsfUtils.getCompPropStringArray(this, getFacesContext(),
				"expandedValues", expandedValuesSet, expandedValues);
	}

	public void setExpandedValues(String[] expandedValues)
	{
		expandedValuesSet = true;
		this.expandedValues = expandedValues;
	}

	public String getOnChange()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"onChange", onChangeSet, onChange);
	}

	public void setOnChange(String onchange)
	{
		onChangeSet = true;
		this.onChange = onchange;
	}

}
