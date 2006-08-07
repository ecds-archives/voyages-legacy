package edu.emory.library.tast.ui;

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

/**
 * This component should not be used any more. Its functionality is provided by
 * {@link edu.emory.library.tast.ui.SectionGroupComponent}.
 * 
 * @author Jan Zich
 * 
 */
public class TabBarComponent extends UIComponentBase
{
	
	private static final String HIDDEN_FIELD_SUFFIX = "_selected_tab";
	private MethodBinding tabChanged;
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
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, tabChanged);
		values[2] = selectedTabId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		tabChanged = (MethodBinding) restoreAttachedState(context, values[1]);
		selectedTabId = (String) values[2];
	}

	public void decode(FacesContext context)
	{
		String newSelectedTabId = (String) context.getExternalContext().getRequestParameterMap().get(getHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0)
		{
			if (!newSelectedTabId.equals(selectedTabId))
			{
				queueEvent(new TabChangeEvent(this, newSelectedTabId));
				selectedTabId = newSelectedTabId;
			}
		}
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedTabId");
		if (vb != null) vb.setValue(context, selectedTabId);
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
		
		if (event instanceof TabChangeEvent && tabChanged != null)
			tabChanged.invoke(getFacesContext(), new Object[] {event});
		
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		if (getSelectedTabId() == null)
		{
			if (getChildCount() > 0)
			{
				setSelectedTabId(((TabComponent) getChildren().get(0)).getTabId());
			}
		}
		
		ResponseWriter writer = context.getResponseWriter();
		
		JsfUtils.encodeHiddenInput(
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
		UIForm form = JsfUtils.getForm(this, context);
		if (form == null) return;
		
		String selectedTabId = getSelectedTabId();
		
		for (Iterator iterChild = getChildren().iterator(); iterChild.hasNext();)
		{
			TabComponent tab = (TabComponent) iterChild.next();
			
			String jsOnClick = JsfUtils.generateSubmitJS(
					context, form,
					getHiddenFieldName(context), tab.getTabId());
			
			writer.startElement("td", this);
			if ((selectedTabId == null && tab.getTabId() == null) || (selectedTabId != null && selectedTabId.equals(tab.getTabId())))
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
	
	public void setSelectedTabId(String tabId)
	{
		if (tabId == null) return;
		selectedTabIdSet = true;
		selectedTabId = tabId;
//		for (Iterator iterChild = getChildren().iterator(); iterChild.hasNext();)
//		{
//			TabComponent tab = (TabComponent) iterChild.next();
//			tab.setSelected(tabId.equals(tab.getTabId()));
//		}
	}
	
	public String getSelectedTabId()
	{
		if (selectedTabIdSet) return selectedTabId;
		ValueBinding vb = getValueBinding("selectedTabId");
		if (vb == null) return selectedTabId;
		return (String) vb.getValue(getFacesContext());
	}
	
	public TabComponent getSelectedTab()
	{
		for (Iterator iterChild = getChildren().iterator(); iterChild.hasNext();)
		{
			TabComponent tab = (TabComponent) iterChild.next();
			if ((selectedTabId == null && tab.getTabId() == null) || (selectedTabId != null && selectedTabId.equals(tab.getTabId())))
			{
				return tab;
			}
		}
		if (getChildCount() > 0)
		{
			return (TabComponent) getChildren().get(0);
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