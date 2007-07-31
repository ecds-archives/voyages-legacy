package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

/**
 * The role of this component is to group some number of expandableBoxes.
 * After expandableBoxes have been grouped, only one of them can be currently opened.
 * After user tries to open some of expandableBoxes, currently expanded box is immediately
 * closed.
 * 
 * This component has tag name expandableBoxSet.
 * See also expandableBox.
 *
 */
public class ExpandableBoxSetComponent extends UIComponentBase {

	public static final String expandedNone = "";
	
	private String expandedId = "";
	private boolean expandedIdSet = false;
	
	public String getFamily() {
		return null;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = expandedId;
		values[2] = new Boolean(expandedIdSet);
		return values;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		expandedId = (String) values[1];
		expandedIdSet = ((Boolean)values[2]).booleanValue();
	}
	
	public void decode(FacesContext context) {
		Map params = context.getExternalContext().getRequestParameterMap();
		expandedId = (String) params.get(getSelectedBoxHiddenFieldName(context));
	}
	
	public void processUpdates(FacesContext context) {
		ValueBinding vb = getValueBinding("expandedId");
		if (vb != null)
			vb.setValue(context, expandedId);
		super.processUpdates(context);
	}

	private String getSelectedBoxHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_selected";
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		
		JsfUtils.encodeHiddenInput(this, writer, getSelectedBoxHiddenFieldName(context), getExpandedId());
		
	}

	public void encodeChildren(FacesContext context) throws IOException {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			ExpandableBoxComponent sect = (ExpandableBoxComponent) children.get(i);
			sect.setFieldId(this.getSelectedBoxHiddenFieldName(context));
			sect.setExpanded(!this.getExpandedId().equals(sect.getBoxId()));
			if (i == 0) {
				sect.setPositionType(ExpandableBoxComponent.SET_TOP_BOX);
			} else if (i < children.size() - 1) {
				sect.setPositionType(ExpandableBoxComponent.SET_MIDDLE_BOX);
			} else {
				sect.setPositionType(ExpandableBoxComponent.SET_BOTTOM_BOX);
			}
			JsfUtils.renderChild(context, sect);
		}
	}
	
	public void encodeEnd(FacesContext arg0) throws IOException {
		ResponseWriter writer = arg0.getResponseWriter();
		writer.endElement("div");
	}
	
	public String getExpandedId() {
		if (this.expandedIdSet) {
			return this.expandedId;
		}
		ValueBinding vb = getValueBinding("expandedId");
		if (vb == null)
			return expandedId;
		return (String) vb.getValue(getFacesContext());
	}

	public void setExpandedId(String expandedId) {
		this.expandedId = expandedId;
		this.expandedIdSet = true;
	}

}
