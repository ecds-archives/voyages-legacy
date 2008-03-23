package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

/**
 * This component groups some number of panel tabs and provides synchronization
 * between them. Basically, it provides tabs functionality to our application.
 * As children of this component one can put panelTab elements. Each panelTab element
 * provides one panel and one tab. The component allows only one panelTab to be opened
 * at a time.
 * Tag for this component used in jsp is panelTabSet.
 *
 */
public class PanelTabSetComponent extends UIComponentBase {

	private String title;

	private String selectedSectionId;

	private boolean selectedSectionIdSet = false;

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = selectedSectionId;
		return values;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		selectedSectionId = (String) values[2];
	}

	public boolean getRendersChildren()
	{
		return true;
	}
	
	public String getFamily()
	{
		return null;
	}

	private String getSelectedTabHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_selected_tab";
	}

	public void decode(FacesContext context)
	{

		Map params = context.getExternalContext().getRequestParameterMap();
		String newSelectedTabId = (String) params.get(getSelectedTabHiddenFieldName(context));
		
		if (!StringUtils.isNullOrEmpty(newSelectedTabId))
		{
			if (!newSelectedTabId.equals(selectedSectionId))
			{
				queueEvent(new TabChangedEvent(this, newSelectedTabId));
				selectedSectionId = newSelectedTabId;
			}
		}

	}

	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedSectionId");
		if (vb != null) vb.setValue(context, selectedSectionId);
		super.processUpdates(context);
	}

	private void encodeTabsTitle(FacesContext context, ResponseWriter writer, UIForm form, String selectedSectionId) throws IOException
	{

		writer.startElement("div", this);
		writer.writeAttribute("class", "tabs-selection", null);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "tabs-selection", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "tabs-tab-first-filler", null);
		writer.endElement("td");

		int tabIdx = 0;
		for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		{
			
			PanelTabComponent sect = (PanelTabComponent) iter.next();
			boolean isSelected = selectedSectionId.equals(sect.getSectionId());
			String cssClassSelectedSuffix = isSelected ? "-selected" : "";

			String href = sect.getHref();
			String jsOnClick;
			
			if (href == null)
			{
				jsOnClick = JsfUtils.generateSubmitJS(
						context, form,
						getSelectedTabHiddenFieldName(context),
						sect.getSectionId());
			}
			else
			{
				jsOnClick = JsfUtils.generateHrefLocationJS(
						href);
			}

			if (tabIdx > 0)
			{
				writer.startElement("td", null);
				writer.writeAttribute("class", "tabs-tab-filler", null);
				writer.endElement("td");
			}
			
			writer.startElement("td", null);
			writer.writeAttribute("class", "tabs-tab-left" + cssClassSelectedSuffix, null);
			writer.endElement("td");

			writer.startElement("td", this);
			writer.writeAttribute("class", "tabs-tab-middle" + cssClassSelectedSuffix, null);
			writer.writeAttribute("onclick", jsOnClick, null);
			writer.write(sect.getTitle());
			writer.endElement("td");
			
			writer.startElement("td", null);
			writer.writeAttribute("class", "tabs-tab-right" + cssClassSelectedSuffix, null);
			writer.endElement("td");
			
			tabIdx++;
			
		}
		
		writer.startElement("td", null);
		writer.writeAttribute("class", "tabs-tab-last-filler", null);
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
		
		writer.endElement("div");

	}
	
	public void encodeChildren(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);

		String selectedSectionId = getSelectedSectionId();
		if (selectedSectionId == null)
			selectedSectionId = "";

		JsfUtils.encodeHiddenInput(this, writer, getSelectedTabHiddenFieldName(context), selectedSectionId);

		encodeTabsTitle(context, writer, form, selectedSectionId);

		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			PanelTabComponent sect = (PanelTabComponent) iter.next();
			if (selectedSectionId.equals(sect.getSectionId())) {
				sect.setRendered(true);
				JsfUtils.renderChild(context, sect);
			} else {
				sect.setRendered(false);
			}
		}

	}

	public String getTitle()
	{
		return JsfUtils.getCompPropString(this, this.getFacesContext(),
				"title", false, title);
	}

	public String getSelectedSectionId()
	{
		return JsfUtils.getCompPropString(this, this.getFacesContext(),
				"selectedSectionId", selectedSectionIdSet, selectedSectionId);
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setSelectedSectionId(String selectedSectionId)
	{
		this.selectedSectionId = selectedSectionId;
		this.selectedSectionIdSet = true;
	}

}
