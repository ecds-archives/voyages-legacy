package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class TabBarComponent extends UIComponentBase
{
	
	private String selectedTabId;
	private boolean selectedTabIdSet = false;
	
	public boolean getRendersChildren()
	{
		return true;
	}
	
	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = selectedTabId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		selectedTabId = (String) values[1];
	}

	private String getHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_selected_tab";
	}

	public void decode(FacesContext context)
	{
		selectedTabId = JsfUtils.getParamString(context, getHiddenFieldName(context));
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedTabId");
		if (vb != null) vb.setValue(context, selectedTabId);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		String selectedTabId = getSelectedTabId();
		
		JsfUtils.encodeHiddenInput(this, writer,
				getHiddenFieldName(context), selectedTabId);

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "tab-bar", null);
		writer.startElement("tr", this);
		
		for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		{
			TabComponent tab = (TabComponent) iter.next();
			
			String jsOnClick = JsfUtils.generateSubmitJS(
					context, form,
					getHiddenFieldName(context), tab.getTabId());
			
			writer.startElement("td", this);
			if (StringUtils.compareStrings(selectedTabId, tab.getTabId()))
			{
				writer.writeAttribute("class", "tab-selected", null);
			}
			else
			{
				writer.writeAttribute("class", "tab", null);
				writer.writeAttribute("onclick", jsOnClick, null);
			}
			writer.write(tab.getText());
			writer.endElement("td");
			
		}
		
		writer.endElement("tr");
		writer.endElement("table");
	
	}
	
	public void setSelectedTabId(String tabId)
	{
		selectedTabIdSet = true;
		selectedTabId = tabId;
	}
	
	public String getSelectedTabId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"selectedTabId", selectedTabIdSet, selectedTabId);
	}

}