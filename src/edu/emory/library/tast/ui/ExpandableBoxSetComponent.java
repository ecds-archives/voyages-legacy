package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class ExpandableBoxSetComponent extends UIComponentBase {

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
		
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			ExpandableBoxComponent sect = (ExpandableBoxComponent) iter.next();
			sect.setFieldId(this.getSelectedBoxHiddenFieldName(context));
			sect.setExpanded(!this.expandedId.equals(sect.getBoxId()));
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
