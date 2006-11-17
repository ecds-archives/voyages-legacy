package edu.emory.library.tast.ui.search.table;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.ui.search.tabscommon.links.TableLinkManager;

public class TableLinksComponent extends UIOutput {

	/**
	 * Restore state overload.
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
	}

	/**
	 * Save state overload.
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[1];
		values[0] = super.saveState(context);
		return values;
	}

	/**
	 * Decode overload.
	 */
	public void decode(FacesContext context) {

		Map params = context.getExternalContext().getRequestParameterMap();

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

		TableLinkManager manager = null;

		ResponseWriter writer = context.getResponseWriter();

		// Start div
		writer.startElement("div", this);

		ValueBinding vb = this.getValueBinding("manager");
		if (vb != null) {
			manager = (TableLinkManager) vb.getValue(context);
		}
		
		writer.endElement("div");
	}
}
