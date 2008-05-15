package edu.emory.library.tast.common.listing;

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
import javax.faces.event.PhaseId;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.Tooltip;
import edu.emory.library.tast.common.listing.TableData.ColumnData;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.query.TastDbQuery;

/**
 * Component used for presenting table result.
 * 
 * 
 */
public class TableResultComponent extends UIOutput
{

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
	public TableResultComponent()
	{
		super();
	}

	/**
	 * Restore state overload.
	 */
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		sortChanged = (MethodBinding) restoreAttachedState(context, values[1]);
		showDetails = (MethodBinding) restoreAttachedState(context, values[2]);
	}

	/**
	 * Save state overload.
	 */
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, sortChanged);
		values[2] = saveAttachedState(context, showDetails);
		return values;
	}

	public void decode(FacesContext context)
	{

		Map params = context.getExternalContext().getRequestParameterMap();

		String newSelectedTabId = (String) params.get(getSortHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0)
		{
			SortChangeEvent sortChangeEvent = new SortChangeEvent(this, newSelectedTabId);
			queueEvent(sortChangeEvent);
		}

		String newSelectedVoyageId = (String) params.get(getClickIdHiddenFieldName(context));
		if (newSelectedVoyageId != null && newSelectedVoyageId.length() > 0)
		{
			ShowDetailsEvent showDetailsEvent = new ShowDetailsEvent(this, new Long(newSelectedVoyageId));
			showDetailsEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
			queueEvent(showDetailsEvent);
		}

	}

	private String appendStyle(String style, String toAppend)
	{
		if (style == null)
		{
			return toAppend;
		}
		else if (!style.trim().equals(""))
		{
			return style + " " + toAppend;
		}
		else
		{
			return toAppend;
		}
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		TableData data = null;
		
		ValueBinding vb = this.getValueBinding("data");
		if (vb != null) data = (TableData) vb.getValue(context);
		
		StringBuffer rowClass = new StringBuffer();
		TableData.DataTableItem[] rows = data.getData();

		UIForm form = JsfUtils.getForm(this, context);
		ResponseWriter writer = context.getResponseWriter();
		
		Tooltip tooltips[][][] = null;
		
		if (rows != null)
		{
		
			tooltips = new Tooltip[rows.length][][]; 
			for (int i = 0; i < rows.length; i++)
			{
				Object voyageId = rows[i].voyageId;
				Object[] columns = rows[i].dataRow;
				tooltips[i] = new Tooltip[columns.length][];
				for (int j = 0; j < columns.length; j++)
				{
					ColumnData column = (ColumnData) columns[j];
					if (column != null)
					{
						String[] formatted = column.getDataToDisplay();
						String[] rollovers = column.getRollovers();
						if (formatted != null && rollovers != null)
						{
							tooltips[i][j] = new Tooltip[formatted.length]; 
							for (int k = 0; k < formatted.length; k++)
							{
								if (rollovers[k] != null)
								{
									Tooltip tooltip = new Tooltip(
											getClientId(context) + "-tooltip-" + voyageId + "-" + k,
											rollovers[k]);
									tooltip.renderTooltip(writer, this);
									tooltips[i][j][k] = tooltip;
								}
							}
						}
					}
				}
			}
			
		}

		// Start div
		writer.startElement("div", this);

		// Bind style/style class.
		vb = this.getValueBinding("style");
		if (vb != null && vb.getValue(context) != null)
		{
			writer.writeAttribute("style", vb.getValue(context), null);
		}
		vb = this.getValueBinding("styleClass");
		if (vb != null && vb.getValue(context) != null)
		{
			writer.writeAttribute("class", vb.getValue(context), null);
		}

		// Encode hidden fields.
		JsfUtils.encodeHiddenInput(this, writer, getSortHiddenFieldName(context));
		JsfUtils.encodeHiddenInput(this, writer, getClickIdHiddenFieldName(context));

		// Start table
		writer.startElement("table", this);
		writer.writeAttribute("class", "grid", null);

		String style = (String) getAttributes().get("style");
		if (style != null)
			writer.writeAttribute("style", style, null);
		String styleClass = (String) getAttributes().get("styleClass");
		if (styleClass != null)
			writer.writeAttribute("class", styleClass, null);
		vb = this.getValueBinding("rendered");
		if (vb != null)
		{
			Boolean b = (Boolean) vb.getValue(context);
			vb = this.getValueBinding("componentVisible");
			if (vb != null)
			{
				vb.setValue(context, b);
			}
		}
		
		writer.startElement("tr", this);
		VisibleAttributeInterface[] populatedAttributes = data.getVisibleAttributes();

		// Encode header of table
		if (populatedAttributes != null)
		{
			for (int i = 0; i < populatedAttributes.length; i++)
			{

				String jsSort = JsfUtils.generateSubmitJS(context, form, getSortHiddenFieldName(context), populatedAttributes[i].encodeToString());

				writer.startElement("th", this);
				writer.startElement("div", null);

				String classStr = "";
				if (i == 0)
					classStr = this.appendStyle(classStr, "grid-first-column");

				if (populatedAttributes[i].getType().equals("NumericAttribute") && i != 0)
				{
					if (data.getOrderByColumn() != null && data.getOrderByColumn().getName().equals(populatedAttributes[i].getName()))
					{

						if (data.getOrder() == TastDbQuery.ORDER_DESC)
						{
							classStr = this.appendStyle(classStr, "grid-header-icon-desc");
						}
						else if (data.getOrder() == TastDbQuery.ORDER_ASC)
						{
							classStr = this.appendStyle(classStr, "grid-header-icon-asc");
						}
					}
					classStr = this.appendStyle(classStr, "grid-header-text-left");
				}
				else
				{
					if (data.getOrderByColumn() != null && data.getOrderByColumn().getName().equals(populatedAttributes[i].getName()))
					{

						if (data.getOrder() == TastDbQuery.ORDER_DESC)
						{
							classStr = this.appendStyle(classStr, "grid-header-icon-desc");
						}
						else if (data.getOrder() == TastDbQuery.ORDER_ASC)
						{
							classStr = this.appendStyle(classStr, "grid-header-icon-asc");
						}
					}
					classStr = this.appendStyle(classStr, "grid-header-text");
				}
				writer.writeAttribute("class", classStr, null);

				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				writer.writeAttribute("onclick", jsSort, null);
				writer.write(populatedAttributes[i].getUserLabelOrName());
				writer.endElement("a");
				writer.endElement("td");

				writer.endElement("div");
				writer.endElement("th");
			}
		}

		writer.endElement("tr");

		if (rows != null)
		{
			
			for (int i = 0; i < rows.length; i++)
			{

				rowClass.setLength(0);
				if (i % 2 == 0)
					rowClass.append("grid-row-even");
				else
					rowClass.append("grid-row-odd");
				if (i == 0)
					rowClass.append(" grid-row-first");
				if (i == rows.length - 1)
					rowClass.append(" grid-row-last");

				Object voyageId = rows[i].voyageId;
				String voyageIdString = voyageId == null ? 
					TastResource.getText("components_table_missingid") :
					voyageId.toString();

				String jsClick = JsfUtils.generateSubmitJS(
						context, form,
						getClickIdHiddenFieldName(context), voyageIdString);

				writer.startElement("tr", this);
				writer.writeAttribute("class", rowClass.toString(), null);
				if (showDetails != null) writer.writeAttribute("onclick", jsClick, null);

				Object[] columns = rows[i].dataRow;
				for (int j = 0; j < columns.length; j++)
				{
					ColumnData column = (ColumnData) columns[j];
					
					writer.startElement("td", this);
					if (populatedAttributes[j].getType().equals("NumericAttribute") && j != 0)
						writer.writeAttribute("style", "text-align: left", null);

					if (column != null)
					{
						String[] formatted = column.getDataToDisplay();
						String[] rollovers = column.getRollovers();

						for (int k = 0; k < formatted.length; k++)
						{

							if (formatted[k] == null)
								continue;

							Tooltip tooltip = rollovers[k] != null ?
								tooltip = tooltips[i][j][k] : null;
								
							if (formatted.length > 1)
								writer.startElement("div", this);
							
							if (tooltip != null)
							{
								writer.startElement("span", this);
								writer.writeAttribute("class", "grid-value-with-tooltip", null);
								tooltip.renderMouseOverEffects(writer);
							}

							writer.write(formatted[k]);

							if (tooltip != null)
								writer.endElement("span");

							if (formatted.length > 1)
								writer.endElement("div");

						}

					}
					writer.endElement("td");

				}
				writer.endElement("tr");

			}

		}

		writer.endElement("table");
		writer.endElement("div");

	}

	private String getSortHiddenFieldName(FacesContext context)
	{
		return this.getClientId(context) + "_sort";
	}

	private String getClickIdHiddenFieldName(FacesContext context)
	{
		return this.getClientId(context) + "_click_id";
	}

	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);

		if (event instanceof SortChangeEvent && sortChanged != null)
		{
			sortChanged.invoke(getFacesContext(), new Object[] { event });
		}

		if (event instanceof ShowDetailsEvent && showDetails != null)
		{
			showDetails.invoke(getFacesContext(), new Object[] { event });
		}

	}

	public MethodBinding getSortChanged()
	{
		return sortChanged;
	}

	public void setSortChanged(MethodBinding sortChanged)
	{
		this.sortChanged = sortChanged;
	}

	public MethodBinding getShowDetails()
	{
		return showDetails;
	}

	public void setShowDetails(MethodBinding showDetails)
	{
		this.showDetails = showDetails;
	}
}
