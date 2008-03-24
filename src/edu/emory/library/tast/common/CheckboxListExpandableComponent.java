package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Set;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class CheckboxListExpandableComponent extends CheckboxListComponent
{
	
	private static final String ARROW_HEIGHT = "11";
	private static final String ARROW_WIDTH = "11";
	private static final String ARROW_EXPANDED = "icon-arrow-down.png";
	private static final String ARROW_COLLAPSED = "icon-arrow-right.png";
	
	private boolean expandedValuesSet = false;
	private String expandedValues[];

	private String getHtmlNameForExpandedValues(FacesContext context)
	{
		return getClientId(context) + "_expanded_values";
	}

	public void decode(FacesContext context)
	{
		super.decode(context);
		if (context.getExternalContext().
				getRequestParameterMap().
				get(getHtmlNameForExpandedValues(context)) != null) {
			expandedValues = (String[]) ((String)
				context.getExternalContext().
				getRequestParameterMap().
				get(getHtmlNameForExpandedValues(context))).split(",");
		}
	}
	
	public void processUpdates(FacesContext context)
	{
		super.processUpdates(context);
		
		ValueBinding vbExpandedValues = getValueBinding("expandedValues");
		if (vbExpandedValues != null && expandedValues != null)
			vbExpandedValues.setValue(context, expandedValues);

	}
	
	private void encodeLevel(FacesContext context, ResponseWriter writer, String mainId, String inputName, SelectItem[] items, Set selectedValuesLookup, Set expandedValuesLookup, int level) throws IOException
	{
		
		// main table
		writer.startElement("table", this);
		writer.writeAttribute("class", "checkbox-list-table-" + level, null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		
		// does any item has subitem?
		boolean hasSubitems = false;
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].hasSubItems())
			{
				hasSubitems = true;
				break;
			}
		}
		
		// items
		for (int i = 0; i < items.length; i++)
		{
			SelectItem item = items[i];
			boolean checked = selectedValuesLookup.contains(item.getValue());
			boolean expanded = expandedValuesLookup.contains(item.getValue());
			String inputId = mainId + "_checkbox_" + item.getValue();
			String subitemsId = mainId + "_subitems_" + item.getValue();
			String imageId = mainId + "_image_" + item.getValue();
			
			// item TR begin
			writer.startElement("tr", this);
			
			// arrow
			if (hasSubitems)
			{

				// onclick
				String onclick =
					"CheckboxListExpandableGlobals.collexp(" +
					"'" + mainId + "'," +
					"'" + item.getValue() + "'," +
					"'" + imageId + "'," +
					"'" + subitemsId + "')";

				// render
				writer.startElement("td", this);
				writer.writeAttribute("class", "checkbox-list-arrow", null);
				if (item.hasSubItems())
				{
					writer.startElement("img", this);
					writer.writeAttribute("id", imageId, null);
					writer.writeAttribute("onclick", onclick, null);
					if (expanded)
					{
						writer.writeAttribute("src", ARROW_EXPANDED, null);
					}
					else
					{
						writer.writeAttribute("src", ARROW_COLLAPSED, null);
					}
					writer.writeAttribute("width", ARROW_WIDTH, null);
					writer.writeAttribute("height", ARROW_HEIGHT, null);
					writer.writeAttribute("border", "0", null);
					writer.writeAttribute("alt", "0", null);
					writer.endElement("img");
				}
				writer.endElement("td");

			}

			// checkbox
			writer.startElement("td", this);
			writer.writeAttribute("class", "checkbox-list-checkbox-" + level, null);
			if (item.isSelectable())
			{
				writer.startElement("input", this);
				writer.writeAttribute("type", "checkbox", null);
				writer.writeAttribute("name", inputName, null);
				writer.writeAttribute("id", inputId, null);
				writer.writeAttribute("value", item.getValue(), null);
				if (checked) writer.writeAttribute("checked", "checked", null);
				writer.endElement("input");
			}
			writer.endElement("td");

			// label
			writer.startElement("td", this);
			writer.writeAttribute("class", "checkbox-list-label-" + level, null);
			writer.startElement("label", this);
			writer.writeAttribute("for", inputId, null);
			writer.write(item.getText());
			writer.endElement("label");
			writer.endElement("td");

			// item TR end
			writer.endElement("tr");
			
			// subitems
			if (item.hasSubItems())
			{
				writer.startElement("tr", this);
				writer.writeAttribute("id", subitemsId, null);
				if (!expanded)
				{
					writer.writeAttribute("style", "display: none", null);
				}
				if (hasSubitems)
				{
					writer.startElement("td", this);
					writer.endElement("td");
				}
				writer.startElement("td", this);
				writer.endElement("td");
				writer.startElement("td", this);
				encodeLevel(context, writer, mainId, inputName, item.getSubItems(), selectedValuesLookup, expandedValuesLookup, level + 1);
				writer.endElement("td");
				writer.endElement("tr");
			}

		}
		
		// end mail table
		writer.endElement("table");
		
	}
	
	
	public void encodeBegin(FacesContext context) throws IOException
	{

		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// get data from beans
		SelectItem[] items = getItems();
		String[] expandedValues = getExpandedValues();
		Set selectedValuesLookup = StringUtils.toStringSet(getSelectedValues());
		Set expandedValuesLookup = StringUtils.toStringSet(expandedValues);
		showSelectAll = isShowSelectAll(); 
		
		// main id for JS
		String mainId = getClientId(context); 
		
		// JS registration
		StringBuffer regJS = new StringBuffer();
		regJS.append("CheckboxListExpandableGlobals.registerCheckboxList(new CheckboxListExpandable(");
		regJS.append("'").append(mainId).append("', ");
		regJS.append("'").append(form.getClientId(context)).append("', ");
		regJS.append("'").append(getHtmlNameForExpandedValues(context)).append("', ");
		regJS.append("'").append(ARROW_EXPANDED).append("', ");
		regJS.append("'").append(ARROW_COLLAPSED).append("', ");
		registerItems(context, mainId, regJS, items);
		regJS.append("))");
		
		// remember expanded values in a hidden field
		JsfUtils.encodeHiddenInput(this, writer,
				getHtmlNameForExpandedValues(context),
				StringUtils.join(",", expandedValues));

		// render registration JS
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);

		// encode recursivelly
		if (items == null) return;
		encodeLevel(context, writer,
				mainId,
				getHtmlNameForSelectemValues(context),
				items,
				selectedValuesLookup,
				expandedValuesLookup, 0);
		
		// select/deselect all
		if (showSelectAll)
			encodeSelectDeselectAll(writer, mainId);
		
	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
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
	
	

}
