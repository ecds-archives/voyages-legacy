package edu.emory.library.tast.ui.search.query;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.dm.Dictionary;

public class DictionaryListComponent extends UIComponentBase
{
	
	private String attributeName;
	private String formName;
	private String hiddenFieldName;
	private String displayFieldName;
	private String updateTotalFieldName;
	private String builderId;
	private Dictionary[] dictionary;

	private boolean attributeNameSet = false;
	private boolean formNameSet = false;
	private boolean hiddenFieldNameSet = false;
	private boolean displayFieldNameSet = false;
	private boolean dictionarySet = false;
	private boolean updateTotalFieldNameSet = false;
	private boolean builderIdSet = false;
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();

		UtilsJSF.encodeJavaScriptStart(this, writer);
		writer.write("var builderId = '" + getBuilderId() + "';\n");
		writer.write("var formName = '" + getFormName() + "';\n");
		writer.write("var attributeName = '" + getAttributeName() + "';\n");
		writer.write("var hiddenFieldName = '" + getHiddenFieldName() + "';\n");
		writer.write("var displayFieldName = '" + getDisplayFieldName() + "';\n");
		writer.write("var updateTotalFieldName = '" + getUpdateTotalFieldName() + "';\n");
		writer.write("var tblListId = '" + getClientId(context) + "';");
		UtilsJSF.encodeJavaScriptEnd(this, writer);

		Dictionary dictionary[] = getDictionary();

		writer.startElement("table", this);
		writer.writeAttribute("id", getClientId(context), null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);

		if (dictionary != null)
		{
			for (int i = 0; i < dictionary.length; i++)
			{
				Dictionary item = dictionary[i];
				writer.startElement("tr", this);
	
				writer.startElement("td", this);
				writer.startElement("input", this);
				writer.writeAttribute("id", "value_" + item.getRemoteId(), null);
				writer.writeAttribute("value", item.getRemoteId(), null);
				writer.writeAttribute("type", "checkbox", null);
				writer.endElement("input");
				writer.endElement("td");
	
				writer.startElement("td", this);
				writer.startElement("label", this);
				writer.writeAttribute("for", "value_" + item.getRemoteId(), null);
				writer.writeAttribute("id", "label_" + item.getRemoteId(), null);
				writer.write(item.getName());
				writer.endElement("label");
				writer.endElement("td");
			
				writer.endElement("tr");
			}
		}
		
		writer.endElement("table");

	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public String getFamily()
	{
		return null;
	}

	public String getAttributeName()
	{
		if (attributeNameSet) return attributeName;
		ValueBinding vb = getValueBinding("attributeName");
		if (vb == null) return attributeName;
		return (String) vb.getValue(getFacesContext());
	}

	public void setAttributeName(String attributeName)
	{
		attributeNameSet = true;
		this.attributeName = attributeName;
	}

	public Dictionary[] getDictionary()
	{
		if (dictionarySet) return dictionary;
		ValueBinding vb = getValueBinding("dictionary");
		if (vb == null) return dictionary;
		return (Dictionary[]) vb.getValue(getFacesContext());
	}

	public void setDictionary(Dictionary[] items)
	{
		dictionarySet = true;
		this.dictionary = items;
	}

	public String getDisplayFieldName()
	{
		if (displayFieldNameSet) return displayFieldName;
		ValueBinding vb = getValueBinding("displayFieldName");
		if (vb == null) return displayFieldName;
		return (String) vb.getValue(getFacesContext());
	}

	public void setDisplayFieldName(String displayFieldName)
	{
		displayFieldNameSet = true;
		this.displayFieldName = displayFieldName;
	}
	
	public String getUpdateTotalFieldName()
	{
		if (updateTotalFieldNameSet) return updateTotalFieldName;
		ValueBinding vb = getValueBinding("updateTotalFieldName");
		if (vb == null) return updateTotalFieldName;
		return (String) vb.getValue(getFacesContext());
	}

	public void setUpdateTotalFieldName(String updateTotalFieldName)
	{
		updateTotalFieldNameSet = true;
		this.updateTotalFieldName = updateTotalFieldName;
	}

	public String getFormName()
	{
		if (formNameSet) return formName;
		ValueBinding vb = getValueBinding("formName");
		if (vb == null) return formName;
		return (String) vb.getValue(getFacesContext());
	}

	public void setFormName(String formName)
	{
		formNameSet = true;
		this.formName = formName;
	}

	public String getBuilderId()
	{
		if (builderIdSet) return builderId;
		ValueBinding vb = getValueBinding("builderId");
		if (vb == null) return builderId;
		return (String) vb.getValue(getFacesContext());
	}

	public void setBuilderId(String builderId)
	{
		builderIdSet = true;
		this.builderId = builderId;
	}
	
	public String getHiddenFieldName()
	{
		if (hiddenFieldNameSet) return hiddenFieldName;
		ValueBinding vb = getValueBinding("hiddenFieldName");
		if (vb == null) return hiddenFieldName;
		return (String) vb.getValue(getFacesContext());
	}

	public void setHiddenFieldName(String hiddenFieldName)
	{
		hiddenFieldNameSet = true;
		this.hiddenFieldName = hiddenFieldName;
	}

}