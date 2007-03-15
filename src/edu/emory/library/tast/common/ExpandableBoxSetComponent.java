package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class ExpandableBoxSetComponent extends UIComponentBase {

	public static final String expandedNone = "expanded-none-jfksdfsdjkalfjaslkfnviaruawe";
	
	private String expandedId = null;
	
	public String getFamily() {
		return null;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = expandedId;
		return values;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		expandedId = (String) values[1];
	}
	
	public void decode(FacesContext context) {
		Map params = context.getExternalContext().getRequestParameterMap();
		this.expandedId = (String) params.get(getSelectedBoxHiddenFieldName(context));
	}

	private String getSelectedBoxHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_selected";
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		
		JsfUtils.encodeHiddenInput(this, writer, getSelectedBoxHiddenFieldName(context), this.expandedId);
		
	}

	public void encodeChildren(FacesContext context) throws IOException {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			ExpandableBoxComponent sect = (ExpandableBoxComponent) children.get(i);
			sect.setFieldId(this.getSelectedBoxHiddenFieldName(context));
			sect.setExpanded(!this.expandedId.equals(sect.getBoxId()));
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

	public void setExpandedId(String expandedId) {
		this.expandedId = expandedId;
	}

}
