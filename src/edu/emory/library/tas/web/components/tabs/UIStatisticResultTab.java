package edu.emory.library.tas.web.components.tabs;

import java.io.IOException;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.VoyageIndex;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.components.pageScroller.UIResultPageScroller;

public class UIStatisticResultTab extends UIOutput implements
		ConditionedTabbedComponent {

	public void appyConditions(Conditions c, FacesContext context) {
		// TODO Auto-generated method stub

	}
	
	public void applyPopulatedAttributes(String[] attributes, FacesContext context) {
		
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

}
