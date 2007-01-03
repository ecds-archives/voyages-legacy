package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class EventLineComponent extends UIComponentBase
{
	
	private static final int HORIZONTAL_LABELS_HEIGHT = 25;
	private static final int VERTICAL_LABELS_WIDTH = 60;
	private static final int EVENTS_HEIGHT = 20;
	private static final int SELECTOR_HEIGHT = 50;
	private static final int HORIZONTAL_LABELS_MARGIN = 10;
	private static final int VERTICAL_LABELS_MARGIN = 10;
	
	private boolean zoomLevelSet = false;
	private int zoomLevel = 0;
	
	private boolean offsetSet = false;
	private int offset = 1500;
	
	private boolean selectorOffsetSet = false;
	private int selectorOffset = 1500;

	private boolean graphHeightSet = false;
	private int graphHeight;

	private boolean viewportHeightSet = false;
	private double viewportHeight;

	private boolean verticalLabelsSet = false;
	private EventLineLabel[] verticalLabels;

	private boolean zoomLevelsSet = false;
	private EventLineZoomLevel[] zoomLevels;

	private boolean graphsSet = false;
	private EventLineGraph[] graphs;
	
	private boolean eventsSet = false;
	private EventLineEvent[] events;

	private boolean eventsColumnsSet = false;
	private int eventsColumns;
	
	public String getFamily()
	{
		return null;
	}
	
	private String getZoomLevelHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_zoom_level";
	}

	private String getOffsetHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_offset";
	}

	private String getEventTextId(FacesContext context, int index)
	{
		return getClientId(context) + "_event_text_" + index;
	}

	private String getLegendValueElementId(FacesContext context, int index)
	{
		return getClientId(context) + "_legend_" + index;
	}

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[6];
		values[0] = super.saveState(context);
		values[1] = new Integer(zoomLevel);
		values[2] = new Integer(offset);
		values[3] = new Integer(graphHeight);
		values[4] = new Integer(selectorOffset);
		values[5] = new Integer(eventsColumns);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		zoomLevel = ((Integer) values[1]).intValue();
		offset = ((Integer) values[2]).intValue();
		graphHeight = ((Integer) values[3]).intValue();
		selectorOffset = ((Integer) values[4]).intValue();
		eventsColumns = ((Integer) values[5]).intValue();
	}
	
	public void decode(FacesContext context)
	{
		zoomLevel = JsfUtils.getParamInt(context, getZoomLevelHiddenFieldName(context), 0);
		offset = JsfUtils.getParamInt(context, getOffsetHiddenFieldName(context), 0);
	}
	
	private void encodeGraphsContainer(ResponseWriter writer, String graphsContainerId, int maxSlots) throws IOException
	{
		
		// graphs container CSS
		String graphsContainerCssStyle =
			"position: absolute; " +
			"left: " + (VERTICAL_LABELS_WIDTH + VERTICAL_LABELS_MARGIN) + "px; " +
			"top: " + (HORIZONTAL_LABELS_HEIGHT + HORIZONTAL_LABELS_MARGIN) + "px; " +
			"height: " + graphHeight + "px";
		
		// graphs container
		writer.startElement("div", this);
		writer.writeAttribute("id", graphsContainerId, null);
		writer.writeAttribute("style", graphsContainerCssStyle, null);
		writer.writeAttribute("class", "event-line-graph-container", null);

		// slots
		for (int i = 0; i < maxSlots; i++)
		{
			writer.startElement("div", this);
			writer.endElement("div");
		}
		
		// graphs container
		writer.endElement("div");
		
	}
	
	private void encodeIndicator(ResponseWriter writer, String indicatorContainerId, String indicatorId, String indicatorLabelId) throws IOException
	{
		
		// style of the indicator container
		String indicatorContainerCssSyle =
			"position: absolute; " +
			"left: " + (VERTICAL_LABELS_WIDTH + VERTICAL_LABELS_MARGIN) + "px; " +
			"top: " + (HORIZONTAL_LABELS_HEIGHT + HORIZONTAL_LABELS_MARGIN) + "px; " +
			"height: " + graphHeight + "px";

		// indicator container
		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorContainerId, null);
		writer.writeAttribute("style", indicatorContainerCssSyle, null);
		writer.writeAttribute("class", "event-line-indicator-container", null);
		
		// style of the indicator
		String indicatorCssSyle = 
			"position: absolute; " +
			"display: none; " +
			"height: " + graphHeight + "px";

		// div line indicator
		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorId, null);
		writer.writeAttribute("style", indicatorCssSyle, null);
		writer.writeAttribute("class", "event-line-indicator", null);
		writer.endElement("div");

		// style of the indicator
		String indicatorLabelCssSyle =
			"display: none; " +
			"position: absolute; " +
			"top: " + graphHeight + "px";

		// label
		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorLabelId, null);
		writer.writeAttribute("style", indicatorLabelCssSyle, null);
		writer.writeAttribute("class", "event-line-indicator-label", null);
		writer.endElement("div");

		// indicator container
		writer.endElement("div");
	}

	private void encodeSelector(ResponseWriter writer, String selectorContainerId, String selectorId, String leftSelectorId, String rightSelectorId, int slots) throws IOException
	{
		
		// selector container CSS
		String selectorContainerStyle =
			"position: absolute; " +
			"left: " + (VERTICAL_LABELS_WIDTH + VERTICAL_LABELS_MARGIN) + "px; " +
			"top: " + (HORIZONTAL_LABELS_HEIGHT + HORIZONTAL_LABELS_MARGIN + graphHeight + EVENTS_HEIGHT) + "px; " +
			"height: " + SELECTOR_HEIGHT + "px";

		// selector container
		writer.startElement("div", this);
		writer.writeAttribute("id", selectorContainerId, null);
		writer.writeAttribute("style", selectorContainerStyle, null);
		writer.writeAttribute("class", "event-line-selector-container", null);
		
		// slots
		for (int i = 0; i < slots; i++)
		{
			writer.startElement("div", this);
			writer.endElement("div");
		}
		
		// selector CSS
		String selectorCssSyle = 
			"position: absolute; " +
			"display: none; " +
			"height: " + SELECTOR_HEIGHT + "px";

		// selector
		writer.startElement("div", this);
		writer.writeAttribute("id", selectorId, null);
		writer.writeAttribute("style", selectorCssSyle, null);
		writer.writeAttribute("class", "event-line-selector", null);
		writer.endElement("div");
		
		// left selector CSS
		String leftCssSyle = 
			"position: absolute; " +
			"display: none; " +
			"height: " + SELECTOR_HEIGHT + "px";

		// selector
		writer.startElement("div", this);
		writer.writeAttribute("id", leftSelectorId, null);
		writer.writeAttribute("style", leftCssSyle, null);
		writer.writeAttribute("class", "event-line-left-selector", null);
		writer.endElement("div");

		// left selector CSS
		String rightCssSyle = 
			"position: absolute; " +
			"display: none; " +
			"height: " + SELECTOR_HEIGHT + "px";

		// selector
		writer.startElement("div", this);
		writer.writeAttribute("id", rightSelectorId, null);
		writer.writeAttribute("style", rightCssSyle, null);
		writer.writeAttribute("class", "event-line-right-selector", null);
		writer.endElement("div");

		// selector container
		writer.endElement("div");

	}

	private void encodeLabels(ResponseWriter writer, String labelsContainerId) throws IOException
	{
		// only container generated here
		writer.startElement("div", this);
		writer.writeAttribute("id", labelsContainerId, null);
		writer.writeAttribute("style", "position: absolute; top: 0px; left: 0px;", null);
		writer.endElement("div");
	}

	private void encodeEvents(FacesContext context, ResponseWriter writer, String mainId, int eventsColumns) throws IOException
	{
		
		if (this.events == null || this.events.length == 0)
			return;
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "event-line-events-title", null);
		writer.write("Events");
		writer.endElement("div");
		
		int eventsPerColumn = this.events.length / eventsColumns;
		if (this.events.length % eventsColumns != 0) eventsPerColumn++;
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "event-line-event-columns", null);
		writer.startElement("tr", this);

		for (int i = 0; i < events.length; i++)
		{
			
			EventLineEvent event = events[i];

			if (i % eventsPerColumn == 0)
			{
				
				if (i > 0) writer.endElement("table");

				if (i > 0) writer.endElement("td");
				writer.startElement("td", this);
				writer.writeAttribute("class", "event-line-event-column", null);

				writer.startElement("table", this);
				writer.writeAttribute("cellspacing", "0", null);
				writer.writeAttribute("cellpadding", "0", null);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("class", "event-line-event-table", null);
				
			}

			if (i % eventsPerColumn != 0)
			{
				writer.startElement("tr", this);
				writer.startElement("td", this);
				writer.writeAttribute("class", "event-line-event-space", null);
				writer.endElement("tr");
				writer.endElement("tr");
			}
			
			String onMouseOver = "EventLineGlobals.highlightEvent(" +
					"'" + mainId + "', " +  i + ")";
			
			String onMouseOut = "EventLineGlobals.blurEvent(" +
					"'" + mainId + "', " +  i + ")";

			writer.startElement("tr", this);
			writer.writeAttribute("id", getEventTextId(context, i), null);
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "event-line-event-table-label", null);
			writer.writeAttribute("onmouseover", onMouseOver, null);
			writer.writeAttribute("onmouseout", onMouseOut, null);
			writer.write(String.valueOf(i+1));
			writer.endElement("td");
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "event-line-event-table-x", null);
			writer.write(String.valueOf(event.getX()));
			writer.endElement("td");

			writer.startElement("td", this);
			writer.writeAttribute("class", "event-line-event-table-text", null);
			writer.write(event.getText());
			writer.endElement("td");

			writer.endElement("tr");
			
		}

		writer.endElement("td");
		writer.endElement("tr");
		writer.endElement("table");
		
	}

	private void encodeLegend(FacesContext context, ResponseWriter writer, String mainId, String horizontalLegedId) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("class", "event-line-legend", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "event-line-legend-horizontal", null);
		//writer.writeAttribute("style", "display: none", null);
		writer.writeAttribute("id", horizontalLegedId, null);
		writer.endElement("td");

		for (int i = 0; i < graphs.length; i++)
		{
			
			EventLineGraph graph = graphs[i];
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "event-line-legend-sample", null);
			writer.startElement("div", this);
			writer.writeAttribute("class", graph.getBaseCssClass(), null);
			writer.endElement("div");
			writer.endElement("td");

			writer.startElement("td", this);
			writer.writeAttribute("class", "event-line-legend-name", null);
			writer.write(graph.getName());
			writer.endElement("td");

			writer.startElement("td", this);
			writer.writeAttribute("class", "event-line-legend-value", null);
			writer.writeAttribute("id", getLegendValueElementId(context, i), null);
			writer.endElement("td");

		}

		writer.endElement("tr");
		writer.endElement("table");

	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);

		// HTML ids
		String mainId = getClientId(context);
		String graphsContainerId = getClientId(context) + "_graphs_contaiter";
		String selectorContainerId = getClientId(context) + "_selector_contaiter";
		String selectorId = getClientId(context) + "_selector";
		String leftSelectorId = getClientId(context) + "_left_selector";
		String rightSelectorId = getClientId(context) + "_right_selector";
		String indicatorContainerId = getClientId(context) + "_indicator_container";
		String indicatorId = getClientId(context) + "_indicator";
		String indicatorLabelId = getClientId(context) + "_indicator_label";
		String labelsContainerId = getClientId(context) + "_labels_container";
		String horizontalLegedId = getClientId(context) + "_horizontal_legend";

		// get data
		zoomLevel = getZoomLevel();
		offset = getOffset();
		graphHeight = getGraphHeight();
		viewportHeight = getViewportHeight();
		selectorOffset = getSelectorOffset();
		graphs = getGraphs();
		events = getEvents();
		zoomLevels = getZoomLevels();
		verticalLabels = getVerticalLabels();
		eventsColumns = getEventsColumns();
		if (graphs == null) graphs = new EventLineGraph[0];
		if (events == null) events = new EventLineEvent[0];
		
		// max view
		EventLineZoomLevel baseZoomLevel = zoomLevels[0];

		// find max number of slots
		int maxSlots = 0;
		for (int i = 0; i < zoomLevels.length; i++)
		{
			int slots = graphs.length * zoomLevels[i].getViewSpan();
			if (slots > maxSlots) maxSlots = slots;
		}
		
		// JS registration
		StringBuffer regJS = new StringBuffer();
		regJS.append("EventLineGlobals.registerEventLine(new EventLine(");
		regJS.append("'").append(mainId).append("', ");
		regJS.append("'").append(form.getClientId(context)).append("', ");
		regJS.append("'").append(graphsContainerId).append("', ");
		regJS.append("'").append(selectorContainerId).append("', ");
		regJS.append("'").append(selectorId).append("', ");
		regJS.append("'").append(leftSelectorId).append("', ");
		regJS.append("'").append(rightSelectorId).append("', ");
		regJS.append("'").append(indicatorContainerId).append("', ");
		regJS.append("'").append(indicatorId).append("', ");
		regJS.append("'").append(indicatorLabelId).append("', ");
		regJS.append("'").append(labelsContainerId).append("', ");
		regJS.append("'").append(horizontalLegedId).append("', ");
		regJS.append("'").append(getZoomLevelHiddenFieldName(context)).append("', ");
		regJS.append("'").append(getOffsetHiddenFieldName(context)).append("', ");
		regJS.append(viewportHeight).append(", ");
		regJS.append(graphHeight).append(", ");
		regJS.append(selectorOffset).append(", ");
		regJS.append(SELECTOR_HEIGHT).append(", ");
		regJS.append(VERTICAL_LABELS_WIDTH).append(", ");
		regJS.append(VERTICAL_LABELS_MARGIN).append(", ");
		regJS.append(HORIZONTAL_LABELS_HEIGHT).append(", ");
		regJS.append(HORIZONTAL_LABELS_MARGIN).append(", ");
		regJS.append(EVENTS_HEIGHT).append(", ");
		
		// JS events
		regJS.append("[");
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineEvent(");
			regJS.append("'").append(getEventTextId(context, i)).append("', ");
			regJS.append(event.getX()).append(", ");
			regJS.append("'").append(JsfUtils.escapeStringForJS(event.getText())).append("'");
			regJS.append(")");
		}
		regJS.append("], ");
		
		// JS graphs
		regJS.append("[");
		for (int i = 0; i < graphs.length; i++)
		{
			EventLineGraph graph = graphs[i];
			int[] x = graph.getX();
			double[] y = graph.getY();
			String[] labels = graph.getLabels();
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineGraph(");
			regJS.append("'").append(JsfUtils.escapeStringForJS(graph.getName())).append("', ");
			regJS.append("'").append(getLegendValueElementId(context, i)).append("', ");
			regJS.append("'").append(graph.getBaseCssClass()).append("', ");
			regJS.append("'").append(graph.getEventOrBaseColor()).append("', ");
			regJS.append(graph.getMaxValue()).append(", ");
			regJS.append(graph.getMinValue()).append(", ");
			regJS.append("[");
			for (int j = 0; j < x.length; j++)
			{
				if (j > 0) regJS.append(", ");
				regJS.append(x[j]);
			}
			regJS.append("], ");
			regJS.append("[");
			for (int j = 0; j < y.length; j++)
			{
				if (j > 0) regJS.append(", ");
				regJS.append(y[j]);
			}
			regJS.append("], ");
			regJS.append("[");
			for (int j = 0; j < y.length; j++)
			{
				if (j > 0) regJS.append(", ");
				regJS.append("'").append(JsfUtils.escapeStringForJS(labels[j])).append("'");
			}
			regJS.append("]");
			regJS.append(")");
		}
		regJS.append("]");
		regJS.append(", ");
		
		// JS zoom levels
		regJS.append("[");
		for (int i = 0; i < zoomLevels.length; i++)
		{
			EventLineZoomLevel zoomLevel = zoomLevels[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineZoomLevel(");
			regJS.append(zoomLevel.getBarWidth()).append(", ");
			regJS.append(zoomLevel.getLabelSpacing()).append(", ");
			regJS.append(zoomLevel.getMajorLabels()).append(", ");
			regJS.append(zoomLevel.getViewSpan());
			regJS.append(")");
		}
		regJS.append("]");
		regJS.append(", ");

		// horizontal labels
		regJS.append("'Year'");
		regJS.append(", ");

		// vertical labels
		regJS.append("[");
		for (int i = 0; i < verticalLabels.length; i++)
		{
			EventLineLabel verticalLabel = verticalLabels[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineLabel(");
			regJS.append(verticalLabel.getValue()).append(", ");
			regJS.append("'").append(JsfUtils.escapeStringForJS(verticalLabel.getLabel())).append("', ");
			regJS.append(verticalLabel.isMajor() ? "true" : "false");
			regJS.append(")");
		}
		regJS.append("]");
		
		// end if JS
		regJS.append("));");

		// render JS
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
		// hidden field with zoomLevel
		JsfUtils.encodeHiddenInput(this, writer,
				getZoomLevelHiddenFieldName(context),
				String.valueOf(zoomLevel));
		
		// hidden field with offset
		JsfUtils.encodeHiddenInput(this, writer,
				getOffsetHiddenFieldName(context),
				String.valueOf(offset));
		
		// main container CSS
		String mainContainerStyle =
			"position: relative; " +
			"width: " + (
					VERTICAL_LABELS_WIDTH + 
					VERTICAL_LABELS_MARGIN +
					baseZoomLevel.getBarWidth() * baseZoomLevel.getViewSpan()) + "px; " + 
			"height: " + (
					HORIZONTAL_LABELS_HEIGHT +
					HORIZONTAL_LABELS_MARGIN +
					graphHeight +
					EVENTS_HEIGHT +
					SELECTOR_HEIGHT +
					HORIZONTAL_LABELS_HEIGHT) + "px;";

		// main div container
		writer.startElement("div", this);
		writer.writeAttribute("id", mainId, null);
		writer.writeAttribute("style", mainContainerStyle, null);
		writer.writeAttribute("class", "event-line-container", null);
		
		// markers
		encodeLabels(writer, labelsContainerId);

		// graphs
		encodeGraphsContainer(writer, graphsContainerId, maxSlots);
		
		// selector
		encodeSelector(writer, selectorContainerId, selectorId, leftSelectorId, rightSelectorId, maxSlots);

		// indicator
		encodeIndicator(writer, indicatorContainerId, indicatorId, indicatorLabelId);

		// end of the main container
		writer.endElement("div");
		
		// render legend
		encodeLegend(context, writer, mainId, horizontalLegedId);
		
		// render events
		encodeEvents(context, writer, mainId, eventsColumns);

	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}

	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public int getGraphHeight()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"graphHeight", graphHeightSet, graphHeight);
	}

	public void setGraphHeight(int height)
	{
		graphHeightSet = true;
		this.graphHeight = height;
	}

	public EventLineGraph[] getGraphs()
	{
		return (EventLineGraph[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"graphs", graphsSet, graphs);
	}

	public void setGraphs(EventLineGraph[] dataSequences)
	{
		graphsSet = true;
		this.graphs = dataSequences;
	}

	public EventLineEvent[] getEvents()
	{
		return (EventLineEvent[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"events", eventsSet, events);
	}

	public void setEvents(EventLineEvent[] items)
	{
		eventsSet = true;
		this.events = items;
	}

	public EventLineLabel[] getVerticalLabels()
	{
		return (EventLineLabel[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"verticalLabels", verticalLabelsSet, verticalLabels);
	}

	public void setVerticalLabels(EventLineLabel[] verticalLabels)
	{
		verticalLabelsSet = true;
		this.verticalLabels = verticalLabels;
	}

	public EventLineZoomLevel[] getZoomLevels()
	{
		return (EventLineZoomLevel[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"zoomLevels", zoomLevelsSet, zoomLevels);
	}

	public void setZoomLevels(EventLineZoomLevel[] zoomLevels)
	{
		zoomLevelsSet = true;
		this.zoomLevels = zoomLevels;
	}

	public int getOffset()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"offset", offsetSet, offset);
	}

	public void setOffset(int offset)
	{
		offsetSet = true;
		this.offset = offset;
	}

	public int getZoomLevel()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"zoomLevel", zoomLevelSet, zoomLevel);
	}

	public void setZoomLevel(int zoomLevel)
	{
		zoomLevelSet = true;
		this.zoomLevel = zoomLevel;
	}

	public int getSelectorOffset()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"selectorOffset", selectorOffsetSet, selectorOffset);
	}

	public void setSelectorOffset(int selectorOffset)
	{
		selectorOffsetSet = true;
		this.selectorOffset = selectorOffset;
	}

	public double getViewportHeight()
	{
		return JsfUtils.getCompPropDouble(this, getFacesContext(),
				"viewportHeight", viewportHeightSet, viewportHeight);
	}

	public void setViewportHeight(int viewportHeight)
	{
		viewportHeightSet = true;
		this.viewportHeight = viewportHeight;
	}

	public int getEventsColumns()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"eventsColumns", eventsColumnsSet, eventsColumns);
	}

	public void setEventsColumns(int eventsColumns)
	{
		eventsColumnsSet = true;
		this.eventsColumns = eventsColumns;
	}

}