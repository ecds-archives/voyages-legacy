package edu.emory.library.tast.common.table;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.common.table.links.LinkElement;
import edu.emory.library.tast.common.table.links.TableLinkManager;
import edu.emory.library.tast.util.JsfUtils;

public class TableLinksComponent extends UIOutput {

	private LinkElement[] links;
	
	/**
	 * Restore state overload.
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		this.links = (LinkElement[])values[1];
	}

	/**
	 * Save state overload.
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = this.links;
		return values;
	}

	/**
	 * Decode overload.
	 */
	public void decode(FacesContext context) {

		Map params = context.getExternalContext().getRequestParameterMap();
		Iterator paramsIter = params.keySet().iterator();
		
		while (paramsIter.hasNext()) {
			String paramName = (String)paramsIter.next();
			if (paramName.startsWith(this.getHiddenFieldName(context)) && !String.valueOf(params.get(paramName)).equals("")) {
				for (int i = 0; i < links.length; i++) {
					if (Integer.parseInt(String.valueOf(params.get(paramName))) == links[i].getId()) {
						ValueBinding vb = this.getValueBinding("manager");
						if (vb != null) {
							TableLinkManager manager = (TableLinkManager) vb.getValue(context);
							manager.clicked(links[i]);
						}
					}
				}
			}
		}
		// String newSelectedTabId = (String)
		// params.get(getSortHiddenFieldName(context));
		// if (newSelectedTabId != null && newSelectedTabId.length() > 0) {
		// queueEvent(new SortChangeEvent(this, newSelectedTabId));
		// }
		//
		// String newSelectedVoyageId = (String)
		// params.get(getClickIdHiddenFieldName(context));
		// if (newSelectedVoyageId != null && newSelectedVoyageId.length() > 0)
		// {
		// queueEvent(new ShowDetailsEvent(this, new
		// Long(newSelectedVoyageId)));
		// }

	}

	/**
	 * Encode begin overload.
	 */
	public void encodeBegin(FacesContext context) throws IOException {

		UIForm form = JsfUtils.getForm(this, context);
		TableLinkManager manager = null;

		ResponseWriter writer = context.getResponseWriter();

		// Start div
//		writer.startElement("div", this);

		ValueBinding vb = this.getValueBinding("manager");
		if (vb != null) {
			manager = (TableLinkManager) vb.getValue(context);
			this.links = manager.getLinks();
		}
		JsfUtils.encodeHiddenInput(this, writer, getHiddenFieldName(context));
		writer.startElement("table", this);
		writer.writeAttribute("class", "td-table-links", null);
		writer.startElement("tr", this);
		for (int i = 0; i < links.length; i++) {
			writer.startElement("td", this);
			if (links[i].isClickable()) {
				writer.writeAttribute("class", links[i].getClassStyle(), null);
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				String jsSort = JsfUtils.generateSubmitJS(context, form, getHiddenFieldName(context),
						links[i].getId() + "");
				writer.writeAttribute("onclick", jsSort, null);
				
				
				writer.write(links[i].getLabel());
				writer.endElement("a");
			} else {
				writer.writeAttribute("class", links[i].getClassStyle(), null);				
				writer.write(links[i].getLabel());
			}
			writer.endElement("td");
		}
		writer.endElement("tr");
		writer.endElement("table");
//		
//		writer.endElement("div");
	}
	
	private String getHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_hidden";
	}
}
