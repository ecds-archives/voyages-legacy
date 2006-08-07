package edu.emory.library.tast.ui.search.table;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.dm.attributes.VisibleColumn;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Component used for presenting table result.
 * @author Pawel Jurczyk
 *
 */
public class UITableResultTab extends UIOutput {

	private static final int TRIM_LENGTH = 35;

	/**
	 * Sort changed binding.
	 */
	private MethodBinding sortChanged;

	/**
	 * Show details binding.
	 */
	private MethodBinding showDetails;

	/**
	 * Default constructor.
	 *
	 */
	public UITableResultTab() {
		super();
	}

	/**
	 * Restore state overload.
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		sortChanged = (MethodBinding) restoreAttachedState(context, values[1]);
		showDetails = (MethodBinding) restoreAttachedState(context, values[2]);
	}

	/**
	 * Save state overload.
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, sortChanged);
		values[2] = saveAttachedState(context, showDetails);
		return values;
	}

	/**
	 * Decode overload.
	 */
	public void decode(FacesContext context) {

		Map params = context.getExternalContext().getRequestParameterMap();

		String newSelectedTabId = (String) params.get(getSortHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0) {
			queueEvent(new SortChangeEvent(this, newSelectedTabId));
		}

		String newSelectedVoyageId = (String) params.get(getClickIdHiddenFieldName(context));
		if (newSelectedVoyageId != null && newSelectedVoyageId.length() > 0) {
			queueEvent(new ShowDetailsEvent(this, new Long(newSelectedVoyageId)));
		}

	}

	/**
	 * Encode begin overload.
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		TableData data = null;

		ResponseWriter writer = context.getResponseWriter();
		
		//Start div
		writer.startElement("div", this);

		//Bind style/style class.
		ValueBinding vb = this.getValueBinding("style");
		if (vb != null && vb.getValue(context) != null) {
			writer.writeAttribute("style", vb.getValue(context), null);
		}
		vb = this.getValueBinding("styleClass");
		if (vb != null && vb.getValue(context) != null) {
			writer.writeAttribute("class", vb.getValue(context), null);
		}

		//Encode hidden fields.
		JsfUtils.encodeHiddenInput(this, writer, getSortHiddenFieldName(context));
		JsfUtils.encodeHiddenInput(this, writer, getClickIdHiddenFieldName(context));

		//Start table
		writer.startElement("table", this);
		writer.writeAttribute("class", "grid", null);

		
		String style = (String) getAttributes().get("style");
		if (style != null)
			writer.writeAttribute("style", style, null);
		String styleClass = (String) getAttributes().get("styleClass");
		if (styleClass != null)
			writer.writeAttribute("class", styleClass, null);
		vb = this.getValueBinding("rendered");
		if (vb != null) {
			Boolean b = (Boolean) vb.getValue(context);
			vb = this.getValueBinding("componentVisible");
			if (vb != null) {
				vb.setValue(context, b);
			}
		}
		vb = this.getValueBinding("data");
		if (vb != null) {
			data = (TableData) vb.getValue(context);
		}
		UIForm form = JsfUtils.getForm(this, context);

		writer.startElement("tr", this);
		VisibleColumn[] populatedAttributes = data.getVisibleAttributes();
		
		//Encode header of table
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {

				String jsSort = JsfUtils.generateSubmitJS(context, form, getSortHiddenFieldName(context),
						populatedAttributes[i].encodeToString());

				writer.startElement("th", this);
				if (i == 0) writer.writeAttribute("class", "grid-first-column", null);

				writer.startElement("table", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("cellspacing", "0", null);
				writer.writeAttribute("cellpadding", "0", null);
				writer.writeAttribute("class", "grid-header", null);
				writer.startElement("tr", this);

				writer.startElement("td", this);
				writer.writeAttribute("class", "grid-header-text", null);
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				writer.writeAttribute("onclick", jsSort, null);
				writer.write(populatedAttributes[i].toString());
				writer.endElement("a");
				writer.endElement("td");

				if (data.getOrderByColumn() != null
						&& data.getOrderByColumn().getId().equals(populatedAttributes[i].getId())) {

					writer.startElement("td", this);
					writer.writeAttribute("class", "grid-header-icon", null);
					if (data.getOrder() == QueryValue.ORDER_DESC) {
						writer.write("<img src=\"up2.gif\" width=\"15\" height=\"15\">");
					} else if (data.getOrder() == QueryValue.ORDER_ASC) {
						writer.write("<img src=\"down2.gif\" width=\"15\" height=\"15\">");
					}
					writer.endElement("td");
				}

				writer.endElement("tr");
				writer.endElement("table");

				writer.endElement("th");

			}
		}
		writer.endElement("tr");

		StringBuffer rowClass = new StringBuffer();
		TableData.DataTableItem[] objs = data.getData();
		
		//Encode data.
		if (objs != null) {
			for (int i = 0; i < objs.length; i++) {

				rowClass.setLength(0);
				if (i % 2 == 0)
					rowClass.append("grid-row-even");
				else
					rowClass.append("grid-row-odd");
				if (i == 0)
					rowClass.append(" grid-row-first");
				if (i == objs.length - 1)
					rowClass.append(" grid-row-last");

				String jsClick = JsfUtils.generateSubmitJS(context, form, getClickIdHiddenFieldName(context),
						objs[i].voyageId.toString().toString());

				writer.startElement("tr", this);
				writer.writeAttribute("class", rowClass.toString(), null);
				writer.writeAttribute("onclick", jsClick, null);
				Object[] values = objs[i].dataRow;
				for (int j = 0; j < values.length; j++) {
					String visibleLabel = null;
					String visibleToolTop = null;
					Object obj = values[j];
					TableData.ColumnData columnData = (TableData.ColumnData)obj;
					if (obj != null) {
						if (columnData.toString().length() > TRIM_LENGTH) {
							visibleLabel = columnData.toString().substring(0, TRIM_LENGTH) + " ...";
							//visibleToolTop = objString.replaceAll(" ", "&nbsp;");
							visibleToolTop = columnData.getToolTipText(data);
						} else {
							visibleLabel = columnData.toString().replaceAll(" ", "&nbsp;");
						}
					}
					writer.startElement("td", this);
					writer.writeAttribute("id", "cell_" + i + "_" + j, null);
					if (j == 0) writer.writeAttribute("class", "grid-first-column", null);
					if (visibleToolTop != null) {
						writer.writeAttribute("onmouseover", "showToolTip('" + "tooltip_" + i + "_" + j + "', " + "'"
								+ "cell_" + i + "_" + j + "')", null);
						writer.writeAttribute("onmouseout", "hideToolTip('" + "tooltip_" + i + "_" + j + "')", null);
					}

					// Tooltip
					if (visibleToolTop != null) {
						writer.startElement("div", this);
						writer.writeAttribute("id", "tooltip_" + i + "_" + j, null);
						writer.writeAttribute("class", "grid-tooltip", null);
						writer.startElement("div", this);
						writer.write(visibleToolTop);
						writer.endElement("div");
						writer.endElement("div");
					}
					
					if (visibleLabel != null) {
						writer.write(visibleLabel);
					}

					writer.endElement("td");
				}

				writer.endElement("tr");

			}
		}

		writer.endElement("table");
	}

	private String getSortHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_sort";
	}

	private String getClickIdHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_click_id";
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);

		if (event instanceof SortChangeEvent && sortChanged != null) {
			sortChanged.invoke(getFacesContext(), new Object[] { event });
		}

		if (event instanceof ShowDetailsEvent && showDetails != null) {
			showDetails.invoke(getFacesContext(), new Object[] { event });
		}

	}

	public MethodBinding getSortChanged() {
		return sortChanged;
	}

	public void setSortChanged(MethodBinding sortChanged) {
		this.sortChanged = sortChanged;
	}

	public MethodBinding getShowDetails() {
		return showDetails;
	}

	public void setShowDetails(MethodBinding showDetails) {
		this.showDetails = showDetails;
	}
}
