package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;


public class TabBarComponent extends UIComponentBase
{
	
	private static final String HIDDEN_FIELD_SUFFIX = "_selected_tab";
	private MethodBinding tabChanged;
	private String lastSelectedTabId;
	
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
		values[1] = saveAttachedState(context, tabChanged);
		values[2] = lastSelectedTabId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		tabChanged = (MethodBinding) restoreAttachedState(context, values[1]);
		lastSelectedTabId = (String) values[2];
	}

	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
		
		if (event instanceof TabChangeEvent && tabChanged != null)
			tabChanged.invoke(getFacesContext(), new Object[] {event});
		
	}

	public void decode(FacesContext context)
	{
		
		String newSelectedTabId = (String) context.getExternalContext().getRequestParameterMap().get(getHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0)
		{
			if (!newSelectedTabId.equals(lastSelectedTabId))
			{
				queueEvent(new TabChangeEvent(this, newSelectedTabId));
				setSelectedTagId(newSelectedTabId);
			}
		}
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		if (getSelectedTagId() == null)
		{
			if (getChildCount() > 0)
			{
				setSelectedTagId(((TabComponent) getChildren().get(0)).getTabId());
			}
		}
		
		ResponseWriter writer = context.getResponseWriter();
		
		UtilsJSF.encodeHiddenInput(
				this, writer,
				getHiddenFieldName(context));

		writer.startElement("div", this);
		writer.writeAttribute("class", "tab-bar", null);

		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.startElement("tr", this);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		if (form == null) return;
		
		for (Iterator iterChild = getChildren().iterator(); iterChild.hasNext();)
		{
			TabComponent tab = (TabComponent) iterChild.next();
			
			String jsOnClick = UtilsJSF.generateSubmitJS(
					context, form,
					getHiddenFieldName(context), tab.getTabId());
			
			writer.startElement("td", this);
			if (tab.isSelected())
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
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");
	
	}
	
	private String getHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + HIDDEN_FIELD_SUFFIX;
	}
	
	public void setSelectedTagId(String tabId)
	{
		if (tabId == null) return;
		lastSelectedTabId = tabId;
		for (Iterator iterChild = getChildren().iterator(); iterChild.hasNext();)
		{
			TabComponent tab = (TabComponent) iterChild.next();
			tab.setSelected(tabId.equals(tab.getTabId()));
		}
	}
	
	public String getSelectedTagId()
	{
		for (Iterator iterChild = getChildren().iterator(); iterChild.hasNext();)
		{
			TabComponent tab = (TabComponent) iterChild.next();
			if (tab.isSelected()) return tab.getTabId();
		}
		return null;
	}

	public MethodBinding getTabChanged()
	{
		return tabChanged;
	}

	public void setTabChanged(MethodBinding tabChanged)
	{
		this.tabChanged = tabChanged;
	}

}