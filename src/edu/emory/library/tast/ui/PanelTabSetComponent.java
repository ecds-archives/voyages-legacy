package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class PanelTabSetComponent extends UIComponentBase {

	private String title;

	private String selectedSectionId;

	private boolean selectedSectionIdSet = false;

	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = selectedSectionId;
		return values;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		selectedSectionId = (String) values[2];
	}

	public boolean getRendersChildren()
	{
		return true;
	}
	
	public String getFamily() {
		return null;
	}

	private String getSelectedTabHiddenFieldName(FacesContext context) {
		return getClientId(context) + "_selected_tab";
	}

	public void decode(FacesContext context) {

		Map params = context.getExternalContext().getRequestParameterMap();

		String newSelectedTabId = (String) params
				.get(getSelectedTabHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0) {
			if (!newSelectedTabId.equals(selectedSectionId)) {
				queueEvent(new TabChangedEvent(this, newSelectedTabId));
				selectedSectionId = newSelectedTabId;
			}
		}

	}

	public void processUpdates(FacesContext context) {
		ValueBinding vb = getValueBinding("selectedSectionId");
		if (vb != null)
			vb.setValue(context, selectedSectionId);
		super.processUpdates(context);
	}

	private void encodeTabsTitle(FacesContext context, ResponseWriter writer,
			UIForm form, String selectedSectionId) throws IOException {

		writer.startElement("div", this);
		writer.writeAttribute("class", "tabs-selection", null);
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "tabs-selection", null);
		writer.startElement("tr", this);

		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			PanelTabComponent sect = (PanelTabComponent) iter.next();
			boolean isSelected = selectedSectionId.equals(sect.getSectionId());

			writeSimpleTd(writer, "tabs-tab-filler");
			writeSimpleTd(writer, "tabs-tab-left" + (isSelected ? "-selected" : ""));
			String tabClass = "tabs-tab-middle" + (isSelected ? "-selected" : "");

			String jsOnClick = JsfUtils
					.generateSubmitJS(context, form,
							getSelectedTabHiddenFieldName(context), sect
									.getSectionId());

			writer.startElement("td", this);
			writer.writeAttribute("class", tabClass, null);
			writer.writeAttribute("onclick", jsOnClick, null);
			writer.write(sect.getTitle());
			writer.endElement("td");
			
			writeSimpleTd(writer, "tabs-tab-right" + (isSelected ? "-selected" : ""));
		}

		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");

	}
	
	private void writeSimpleTd(ResponseWriter writer, String styleClass) throws IOException {
		writer.startElement("td", null);
		writer.writeAttribute("class", styleClass, null);
		writer.endElement("td");
	}

	public void encodeChildren(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);

		String selectedSectionId = getSelectedTabId();
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

	public String getTitle() {
		String val = JsfUtils.getCompPropString(this, this.getFacesContext(),
				"title", false, title);
		return val;
	}

	public String getSelectedSectionId() {
		return selectedSectionId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSelectedTabId() {
		if (selectedSectionIdSet)
			return selectedSectionId;
		ValueBinding vb = getValueBinding("selectedSectionId");
		if (vb == null)
			return selectedSectionId;
		return (String) vb.getValue(getFacesContext());
	}

	public void setSelectedSectionId(String selectedSectionId) {
		this.selectedSectionId = selectedSectionId;
	}

}
