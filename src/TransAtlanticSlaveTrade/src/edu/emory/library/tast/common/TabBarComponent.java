package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class TabBarComponent extends UIComponentBase
{
	
	private String selectedTabId;
	private boolean selectedTabIdSet = false;
	
	private MethodBinding onTabChanged;
	
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
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = selectedTabId;
		values[2] = saveAttachedState(context, onTabChanged);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		selectedTabId = (String) values[1];
		onTabChanged = (MethodBinding) restoreAttachedState(context, values[2]);
	}

	private String getHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_selected_tab";
	}

	public void decode(FacesContext context)
	{
		String newSelectedTabId = JsfUtils.getParamString(context, getHiddenFieldName(context));
		if (!StringUtils.compareStrings(newSelectedTabId, selectedTabId))
		{
			selectedTabId = newSelectedTabId;
			queueEvent(new TabChangedEvent(this, selectedTabId));
		}
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		
		if (event instanceof TabChangedEvent)
			if (onTabChanged != null)
				onTabChanged.invoke(getFacesContext(), new Object[] {event});
		
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
		
		selectedTabId = getSelectedTabId();
		
		JsfUtils.encodeHiddenInput(this, writer,
				getHiddenFieldName(context), selectedTabId);

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "tabs", null);
		writer.startElement("tr", this);
		
		int i = 0;
		for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		{
			
			TabComponent tab = (TabComponent) iter.next();
			
			String jsOnClick = JsfUtils.generateSubmitJS(
					context, form,
					getHiddenFieldName(context), tab.getTabId());
			
			if (i > 0)
			{
				writer.startElement("td", this);
				writer.writeAttribute("class", "tabs-separator", null);
				writer.endElement("td");
			}
			
			writer.startElement("td", this);
			if (StringUtils.compareStrings(selectedTabId, tab.getTabId()))
			{
				writer.writeAttribute("class", "tabs-tab-active", null);
			}
			else
			{
				writer.writeAttribute("class", "tabs-tab", null);
				writer.writeAttribute("onclick", jsOnClick, null);
			}
			writer.write(tab.getText());
			writer.endElement("td");
			
			i++;
			
		}
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "tabs-right-extent", null);
		writer.write("&nbsp;");
		writer.endElement("td");

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

	public MethodBinding getOnTabChanged()
	{
		return onTabChanged;
	}

	public void setOnTabChanged(MethodBinding onTabChanged)
	{
		this.onTabChanged = onTabChanged;
	}

}