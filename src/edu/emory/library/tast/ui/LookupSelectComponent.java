package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class LookupSelectComponent extends UIComponentBase
{
	
	private boolean sourceIdSet = false;
	private String sourceId;
	
	private boolean selectedValuesSet = false;
	private String[] selectedValues = new String[0];
	
	public String getFamily()
	{
		return null;
	}
	
	private String getFieldNameForSelectedValues(FacesContext context)
	{
		return getClientId(context) + "_selected_values";
	}
	
	public void decode(FacesContext context)
	{
		Map params = context.getExternalContext().getRequestParameterMap();
		
		String selectedValuesStr = (String) params.get(getFieldNameForSelectedValues(context));
		if (!StringUtils.isNullOrEmpty(selectedValuesStr))
			selectedValues = selectedValuesStr.split(",");
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// <select> name
		String selectName = getClientId(context) + "_select";
		
		// load selected items
		sourceId = getSourceId();
		selectedValues = getSelectedValues();
		LookupSource source = LookupSources.getLookupSource(sourceId);
		List selectedItems = source.getItemsByValues(selectedValues);

		// registration script
		StringBuffer regJS = new StringBuffer();
		regJS.append("LookupSelectGlobals.registerLookup(new LookupSelect(");
		regJS.append("'").append(getClientId(context)).append("', ");
		regJS.append("'").append(form.getClientId(context)).append("', ");
		regJS.append("'").append(sourceId).append("', ");
		regJS.append("'").append(selectName).append("', ");
		regJS.append("'").append(getFieldNameForSelectedValues(context)).append("'");
		regJS.append("))");
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
		// add JS
		String jsAdd =
			"LookupSelectGlobals.openLookupWindow(" +
			"'" + getClientId(context) + "')";
		
		// remove JS
		String jsRemove =
			"LookupSelectGlobals.removeSelectedItems(" +
			"'" + getClientId(context) + "')";

		// a field for selected values
		JsfUtils.encodeHiddenInput(this, writer,
				getFieldNameForSelectedValues(context),
				StringUtils.join(selectedValues, ","));

		// render the list of items
		writer.startElement("select", this);
		writer.writeAttribute("name", selectName, null);
		writer.writeAttribute("style", "width: 200px;", null);
		writer.writeAttribute("size", "10", null);
		writer.writeAttribute("multiple", "multiple", null);
		for (Iterator iter = selectedItems.iterator(); iter.hasNext();)
		{
			javax.faces.model.SelectItem item = (javax.faces.model.SelectItem) iter.next();
			writer.startElement("option", this);
			writer.writeAttribute("value", item.getValue(), null);
			writer.write(item.getLabel());
			writer.endElement("option");
		}
		writer.endElement("select");
		
		// temporary
		writer.startElement("br", this);
		writer.endElement("br");
		
		// add button
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", "Add", null);
		writer.writeAttribute("onclick", jsAdd, null);
		writer.endElement("input");
		
		// add button
		writer.startElement("input", this);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", "Remove", null);
		writer.writeAttribute("onclick", jsRemove, null);
		writer.endElement("input");

	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedValues");
		if (vb != null) vb.setValue(context, selectedValues);
	}

	public void setSourceId(String lookupId)
	{
		sourceIdSet = true;
		this.sourceId = lookupId;
	}

	public String getSourceId()
	{
		if (sourceIdSet) return sourceId;
		ValueBinding vb = getValueBinding("sourceId");
		if (vb == null) return sourceId;
		return (String) vb.getValue(getFacesContext());
	}

	public void setSelectedValues(String[] selectedValues)
	{
		selectedValuesSet = true;
		this.selectedValues = selectedValues;
	}

	public String[] getSelectedValues()
	{
		if (selectedValuesSet) return selectedValues;
		ValueBinding vb = getValueBinding("selectedValues");
		if (vb == null) return selectedValues;
		return (String[]) vb.getValue(getFacesContext());
	}

}