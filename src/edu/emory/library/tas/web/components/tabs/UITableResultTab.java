package edu.emory.library.tas.web.components.tabs;

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

import edu.emory.library.tas.attrGroups.VisibleColumn;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.UtilsJSF;

public class UITableResultTab extends UIOutput {

	private static final int TRIM_LENGTH = 35;
	private MethodBinding sortChanged;
	private MethodBinding showDetails;

	public UITableResultTab() {
		super();
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		sortChanged = (MethodBinding) restoreAttachedState(context, values[1]);
		showDetails = (MethodBinding) restoreAttachedState(context, values[2]);
	}

	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, sortChanged);
		values[2] = saveAttachedState(context, showDetails);
		return values;
	}

	public void decode(FacesContext context) {

		Map params = context.getExternalContext().getRequestParameterMap();

		String newSelectedTabId = (String) params
				.get(getSortHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0) {
			queueEvent(new SortChangeEvent(this, newSelectedTabId));
		}

		String newSelectedVoyageId = (String) params
				.get(getClickIdHiddenFieldName(context));
		if (newSelectedVoyageId != null && newSelectedVoyageId.length() > 0) {
			queueEvent(new ShowDetailsEvent(this, new Long(
					newSelectedVoyageId)));
		}

	}

	public void encodeBegin(FacesContext context) throws IOException {
		TableData data = null;

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);

		UtilsJSF.encodeHiddenInput(this, writer,
				getSortHiddenFieldName(context));
		UtilsJSF.encodeHiddenInput(this, writer,
				getClickIdHiddenFieldName(context));

		writer.startElement("table", this);
		writer.writeAttribute("class", "grid", null);

		String style = (String) getAttributes().get("style");
		if (style != null)
			writer.writeAttribute("style", style, null);

		String styleClass = (String) getAttributes().get("styleClass");
		if (styleClass != null)
			writer.writeAttribute("class", styleClass, null);

		ValueBinding vb = this.getValueBinding("rendered");
		if (vb != null) {
			Boolean b = (Boolean) vb.getValue(context);
			vb = this.getValueBinding("componentVisible");
			if (vb != null) {
				vb.setValue(context, b);
			}
		}

		vb = this.getValueBinding("conditions");
		if (vb != null) {
			Conditions c = (Conditions) vb.getValue(context);
			vb = this.getValueBinding("conditionsOut");
			if (vb != null) {
				vb.setValue(context, c);
			}
		}

		vb = this.getValueBinding("data");
		if (vb != null) {
			data = (TableData) vb.getValue(context);
		}

		UIForm form = UtilsJSF.getForm(this, context);

		writer.startElement("tr", this);
		VisibleColumn[] populatedAttributes = data.getVisibleAttributes();
		if (populatedAttributes != null) {
			for (int i = 0; i < populatedAttributes.length; i++) {

				String jsSort = UtilsJSF.generateSubmitJS(context, form,
						getSortHiddenFieldName(context), populatedAttributes[i]
								.encodeToString());

				writer.startElement("th", this);

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
						&& data.getOrderByColumn().getId().equals(
								populatedAttributes[i].getId()))
				{
					
					writer.startElement("td", this);
					writer.writeAttribute("class", "grid-header-icon", null);
					if (data.getOrder() == QueryValue.ORDER_DESC)
					{
						writer.write("<img src=\"up2.gif\" width=\"15\" height=\"15\">");
					}
					else if (data.getOrder() == QueryValue.ORDER_ASC)
					{
						writer.write("<img src=\"down2.gif\" width=\"15\" height=\"15\">");
					}
					writer.endElement("td");
				}

				writer.endElement("tr");
				writer.endElement("table");

				writer.endElement("th");

			}
		}
//		writer.startElement("th", this);
//		writer.endElement("th");
		writer.endElement("tr");

		StringBuffer rowClass = new StringBuffer();
		TableData.DataTableItem[] objs = data.getData();
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

				String jsClick = UtilsJSF.generateSubmitJS(context, form,
						getClickIdHiddenFieldName(context), objs[i].voyageId.toString()
								.toString());

				writer.startElement("tr", this);
				writer.writeAttribute("class", rowClass.toString(), null);
				writer.writeAttribute("onclick", jsClick, null);
				Object[] values = objs[i].dataRow;
				for (int j = 0; j < values.length; j++) {
					String visibleLabel = null;
					String visibleToolTop = null;
					Object obj = values[j];
					if (obj != null) {
						if (obj.toString().length() > TRIM_LENGTH) {
							visibleLabel = obj.toString().substring(0, TRIM_LENGTH) + " ...";
							visibleToolTop = obj.toString();
						} else {
							visibleLabel = obj.toString();
						}
					}
					writer.startElement("td", this);
					writer.writeAttribute("id", "cell_" + i + "_" + j, null);
					if (visibleToolTop != null) {
						writer.writeAttribute("onmouseover", 
							"showToolTip('" + "tooltip_" + i + "_" + j + "', " +
									"'" + "cell_" + i + "_" + j + "')", null);
						writer.writeAttribute("onmouseout", 
							"hideToolTip('" + "tooltip_" + i + "_" + j + "')", null);
					}
					
					if (visibleLabel != null) {
						writer.write(visibleLabel);
					}
					
					//Tooltip
					if (visibleToolTop != null) {
						writer.startElement("div", this);
						writer.writeAttribute("id", "tooltip_" + i + "_" + j, null);
						writer.writeAttribute("class", "tableDataTooltip", null);
						if (obj != null) {
							writer.write(visibleToolTop.replaceAll("', '", "',  <br>'"));
						}
						writer.endElement("div");
					}
					
					writer.endElement("td");
				}
				
//				String jsShowDetail = UtilsJSF.generateSubmitJS(context, form,
//						getClickIdHiddenFieldName(context), objs[i].voyageId.toString());
//				writer.startElement("td", this);
//				writer.startElement("a", this);
//				writer.writeAttribute("href", "#", null);
//				writer.writeAttribute("style", "border: 0px;", null);
//				writer.writeAttribute("onclick", jsShowDetail, null);
//				writer.write("<img style=\"border: 0px;\" alt=\"Details of voyage\" " +
//						"src=\"contents.gif\" width=\"12\" height=\"15\">");
//				writer.endElement("a");
//				writer.endElement("td");
				
				writer.endElement("tr");

			}
		}

		writer.endElement("table");
	}

	// private String prepareJS(FacesContext context, String pressedLink) {
	// StringBuffer buffer = new StringBuffer();
	// String ID = getSortHiddenFieldName(context);
	// buffer.append(" onclick=\"document.forms['form'].elements['" + ID +
	// "'].value = '"
	// + pressedLink + "';" +
	// "clear_form();form.submit();\"");
	// return buffer.toString();
	// }

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
