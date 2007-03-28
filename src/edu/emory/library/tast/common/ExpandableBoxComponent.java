package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

/**
 * Component which provides expandable panel for applications.
 * Expandable panel can be either collapsed or expanded.
 * Used in almost all pages.
 * Component name in jsp pages is expandableBox.
 * 
 * This component can be child of expandableBoxSet component.
 *
 */
public class ExpandableBoxComponent extends UIComponentBase {

	public static final int SET_TOP_BOX = 1;
	public static final int SET_MIDDLE_BOX = 2;
	public static final int SET_BOTTOM_BOX = 3;
	
	private static final String EXPANEDED = "expanded";

	private static final String COLLAPSED = "collapsed";

	private Boolean collapsed = new Boolean(false);

	private String fieldId = null;

	private String text = "";

	private boolean textSet = false;

	private String boxId = null;
	private int positionType;

	public String getFamily() {
		return null;
	}

	public boolean getRendersChildren() {
		return true;
	}

	public Object saveState(FacesContext context) {
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = text;
		values[2] = collapsed;
		values[3] = boxId;
		values[4] = new Integer(positionType);
		return values;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		text = (String) values[1];
		collapsed = (Boolean) values[2];
		boxId = (String) values[3];
		positionType = ((Integer)values[4]).intValue();
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
		if (this.fieldId == null) {
			writeSimpleTd(writer, "box-upper-row-left"
				+ (isCollapsed() ? "-collapsed" : ""));
		} else {
			writeSimpleTd(writer, "box-set-upper-row-" + this.getBoxTypePrefix() + "-left"
					+ (isCollapsed() ? "-collapsed" : ""));
		}
		writer.startElement("td", null);
		if (this.fieldId == null) {
			writer.writeAttribute("class", "box-upper-row-middle"
				+ (isCollapsed() ? "-collapsed" : ""), null);
		} else {
			writer.writeAttribute("class", "box-set-upper-row-" + this.getBoxTypePrefix() + "-middle"
					+ (isCollapsed() ? "-collapsed" : ""), null);
		}
		writeHeaderTable(context, writer);
		writer.endElement("td");
		if (this.fieldId == null) {
			writeSimpleTd(writer, "box-upper-row-right"
				+ (isCollapsed() ? "-collapsed" : ""));
		} else {
			writeSimpleTd(writer, "box-set-upper-row-" + this.getBoxTypePrefix() + "-right"
					+ (isCollapsed() ? "-collapsed" : ""));
		}
		writer.endElement("tr");

		if (!isCollapsed()) {
			writer.startElement("tr", null);

			writeSimpleTd(writer, "box-middle-row-left", true);

			writer.startElement("td", null);
			writer.writeAttribute("class", "box-middle-row-middle", null);
			writer.startElement("div", null);
			writer.writeAttribute("id", getClientId(context), null);
			writer.writeAttribute("class", "box-main-text", null);
		}

	}

	private String getBoxTypePrefix() {
		if (this.positionType == SET_BOTTOM_BOX) {
			return "bottom";
		} else if (this.positionType == SET_MIDDLE_BOX) {
			return "middle";
		} else {
			return "upper";
		}
	}

	public void encodeChildren(FacesContext context) throws IOException {
        if (getChildCount() > 0)
        {
            for (Iterator it = getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                child.setRendered(!isCollapsed());
            }
        }
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
			} else {
				JsfUtils.appendSubmitJS(js, context, form,
						this.fieldId, ExpandableBoxSetComponent.expandedNone);
			}
		}
		writer.writeAttribute("onclick", js, null);
		writer.endElement("div");
		writer.endElement("td");
		writer.endElement("tr");

		writer.endElement("table");
	}

	private void writeSimpleTd(ResponseWriter writer, String styleClass) throws IOException {
		this.writeSimpleTd(writer, styleClass, false);
	}
	
	private void writeSimpleTd(ResponseWriter writer, String styleClass, boolean div)
			throws IOException {
		writer.startElement("td", null);
		if (!div) {
			writer.writeAttribute("class", styleClass, null);
		} else {
			writer.writeAttribute("class", styleClass, null);
			writer.startElement("div", null);
			writer.writeAttribute("class", styleClass, null);
			writer.endElement("div");
		}
		writer.endElement("td");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		if (!isCollapsed()) {
			writer.endElement("div");
			writer.endElement("td");
			writeSimpleTd(writer, "box-middle-row-right", true);
			writer.endElement("tr");
			writer.startElement("tr", null);
			if (this.fieldId == null) {
				writeSimpleTd(writer, "box-lower-row-left");
			} else {
				writeSimpleTd(writer, "box-set-lower-row-" + getBoxTypePrefix() + "-left");
			}
			if (this.fieldId == null) {
				writeSimpleTd(writer, "box-lower-row-middle");
			} else {
				writeSimpleTd(writer, "box-set-lower-row-" + getBoxTypePrefix() + "-middle");
			}
			if (this.fieldId == null) {
				writeSimpleTd(writer, "box-lower-row-right");
			} else {
				writeSimpleTd(writer, "box-set-lower-row-" + getBoxTypePrefix() + "-right");
			}
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

	public void setPositionType(int position) {
		this.positionType = position;
	}

}
