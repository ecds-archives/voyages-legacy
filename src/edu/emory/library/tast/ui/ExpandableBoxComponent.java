package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class ExpandableBoxComponent extends UIComponentBase {

	private static final String EXPANEDED = "expanded";

	private static final String COLLAPSED = "collapsed";

	private Boolean collapsed = new Boolean(false);

	private String fieldId = null;

	private String text = "";

	private boolean textSet = false;

	private String boxId = null;

	public String getFamily() {
		return null;
	}

	public boolean getRendersChildren() {
		return true;
	}

	public Object saveState(FacesContext context) {
		Object[] values = new Object[4];
		values[0] = super.saveState(context);
		values[1] = text;
		values[2] = collapsed;
		values[3] = boxId;
		return values;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		text = (String) values[1];
		collapsed = (Boolean) values[2];
		boxId = (String) values[3];
	}

	private String getStateHiddenFieldName(FacesContext context) {
		return getClientId(context) + "_state";
	}

	public void decode(FacesContext context) {

		ExternalContext externalContex = context.getExternalContext();

		String stateStr = (String) externalContex.getRequestParameterMap().get(
				getStateHiddenFieldName(context));

		if (fieldId == null) {
			collapsed = new Boolean(COLLAPSED.equals(stateStr));
		}
	}

	public void encodeBegin(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("table", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "box-main-table", null);

		writer.startElement("tr", null);
		writeSimpleTd(writer, "box-upper-row-left"
				+ (isCollapsed() ? "-collapsed" : ""));
		writer.startElement("td", null);
		writer.writeAttribute("class", "box-upper-row-middle"
				+ (isCollapsed() ? "-collapsed" : ""), null);
		writeHeaderTable(context, writer);
		writer.endElement("td");
		writeSimpleTd(writer, "box-upper-row-right"
				+ (isCollapsed() ? "-collapsed" : ""));
		writer.endElement("tr");

		if (!isCollapsed()) {
			writer.startElement("tr", null);

			writeSimpleTd(writer, "box-middle-row-left");

			writer.startElement("td", null);
			writer.writeAttribute("class", "box-middle-row-middle", null);
			writer.startElement("div", null);
			writer.writeAttribute("id", getClientId(context), null);
			writer.writeAttribute("class", "box-main-text", null);
		}

	}

	public void encodeChildren(FacesContext context) throws IOException {
		if (!isCollapsed()) {
			JsfUtils.renderChildren(context, this);
		}
	}

	private void writeHeaderTable(FacesContext context, ResponseWriter writer)
			throws IOException {

		if (this.fieldId == null) {
			JsfUtils.encodeHiddenInput(this, writer,
					getStateHiddenFieldName(context), isCollapsed() ? COLLAPSED
							: EXPANEDED);
		}

		writer.startElement("table", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "box-header-table", null);

		writer.startElement("tr", null);
		writer.startElement("td", null);
		writer.writeAttribute("class", "box-label", null);
		writer.write(getText());
		writer.endElement("td");
		writer.startElement("td", null);
		writer.startElement("div", null);
		if (isCollapsed()) {
			writer.writeAttribute("class", "box-button-collapsed", null);
		} else {
			writer.writeAttribute("class", "box-button", null);
		}
		
		StringBuffer js = new StringBuffer();
		UIForm form = JsfUtils.getForm(this, context);
		if (isCollapsed()) {
			if (this.fieldId == null) {
				JsfUtils.appendSubmitJS(js, context, form,
						getStateHiddenFieldName(context), EXPANEDED);
			} else {
				JsfUtils.appendSubmitJS(js, context, form,
						this.fieldId, this.getBoxId());
			}
		} else {
			if (this.fieldId == null) {
				JsfUtils.appendSubmitJS(js, context, form,
						getStateHiddenFieldName(context), COLLAPSED);
			}
		}
		writer.writeAttribute("onclick", js, null);
		writer.endElement("div");
		writer.endElement("td");
		writer.endElement("tr");

		writer.endElement("table");
	}

	private void writeSimpleTd(ResponseWriter writer, String styleClass)
			throws IOException {
		writer.startElement("td", null);
		writer.writeAttribute("class", styleClass, null);
		writer.endElement("td");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		if (!isCollapsed()) {
			writer.endElement("div");
			writer.endElement("td");
			writeSimpleTd(writer, "box-middle-row-right");
			writer.endElement("tr");
			writer.startElement("tr", null);
			writeSimpleTd(writer, "box-lower-row-left");
			writeSimpleTd(writer, "box-lower-row-middle");
			writeSimpleTd(writer, "box-lower-row-right");
			writer.endElement("tr");
		}

		writer.endElement("table");
	}

	public String getText() {
		if (textSet)
			return text;
		ValueBinding vb = getValueBinding("text");
		if (vb == null)
			return text;
		return (String) vb.getValue(getFacesContext());
	}

	public void setText(String text) {
		textSet = true;
		this.text = text;
	}

	public void setCollapsed(String collapsed) {
		if (collapsed != null) {
			this.collapsed = new Boolean(collapsed.equals("true"));
		}
	}

	public void setExpanded(boolean expanded) {
		this.collapsed = new Boolean(expanded);
	}

	public boolean isCollapsed() {
		return collapsed.booleanValue();
	}

	public void setFieldId(String selectedBoxHiddenFieldName) {
		this.fieldId = selectedBoxHiddenFieldName;
	}

	public void setBoxId(String boxId) {
		this.boxId  = boxId;
	}

	public String getBoxId() {
		return boxId;
	}

}
