package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class EventLineComponent extends UIComponentBase
{
	
	private static final int TOP_LABELS_HEIGHT = 25;
	// private static final int TOP_LABELS_WIDTH = 50;
	private static final int LEFT_LABELS_WIDTH = 60;
	// private static final int LEFT_LABELS_HEIGHT = 30;
	private static final int LABELS_MARGIN = 10;
	private static final int SELECTOR_HEIGHT = 50;
	
	private boolean zoomLevelSet = false;
	private int zoomLevel = 0;
	
	private boolean offsetSet = false;
	private int offset = 1500;
	
	private boolean selectorOffsetSet = false;
	private int selectorOffset = 1500;

	private boolean graphHeightSet = false;
	private int graphHeight;

	private boolean viewportHeightSet = false;
	private int viewportHeight;

	private boolean verticalLabelsSet = false;
	private EventLineVerticalLabels verticalLabels;

	private boolean zoomLevelsSet = false;
	private EventLineZoomLevel[] zoomLevels;

	private boolean graphsSet = false;
	private EventLineGraph[] graphs;
	
	private boolean eventsSet = false;
	private EventLineEvent[] events;
	
	public String getFamily()
	{
		return null;
	}
	
	private String getEventMarkElementId(FacesContext context, int index)
	{
		return getClientId(context) + "_event_mark_" + index;
	}

	private String getZoomLevelHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_zoom_level";
	}

	private String getOffsetHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_offset";
	}

	private String getEventTextElementId(FacesContext context, int index)
	{
		return getClientId(context) + "_event_text_" + index;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = new Integer(zoomLevel);
		values[2] = new Integer(offset);
		values[3] = new Integer(graphHeight);
		values[4] = new Integer(selectorOffset);
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
			"left: " + (LEFT_LABELS_WIDTH + LABELS_MARGIN) + "px; " +
			"top: " + (TOP_LABELS_HEIGHT + LABELS_MARGIN) + "px; " +
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
			"left: " + (LEFT_LABELS_WIDTH + LABELS_MARGIN) + "px; " +
			"top: " + (TOP_LABELS_HEIGHT + LABELS_MARGIN) + "px; " +
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
			"left: " + (LEFT_LABELS_WIDTH + LABELS_MARGIN) + "px; " +
			"top: " + (TOP_LABELS_HEIGHT + LABELS_MARGIN + graphHeight) + "px; " +
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

		// these data won't be saved in the component state
		zoomLevel = getZoomLevel();
		offset = getOffset();
		graphHeight = getGraphHeight();
		viewportHeight = getViewportHeight();
		selectorOffset = getSelectorOffset();
		EventLineGraph[] graphs = getGraphs();
		EventLineEvent[] events = getEvents();
		EventLineZoomLevel[] zoomLevels = getZoomLevels();
		EventLineVerticalLabels verticalLabels = getVerticalLabels();
		if (graphs == null) graphs = new EventLineGraph[0];
		if (events == null) events = new EventLineEvent[0];

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
		regJS.append("'").append(getZoomLevelHiddenFieldName(context)).append("', ");
		regJS.append("'").append(getOffsetHiddenFieldName(context)).append("', ");
		regJS.append(viewportHeight).append(", ");
		regJS.append(graphHeight).append(", ");
		regJS.append(selectorOffset).append(", ");
		regJS.append(SELECTOR_HEIGHT).append(", ");
		regJS.append(LEFT_LABELS_WIDTH).append(", ");
		regJS.append(LABELS_MARGIN).append(", ");
		regJS.append(TOP_LABELS_HEIGHT).append(", ");
		regJS.append(LABELS_MARGIN).append(", ");
		
		// JS events
		regJS.append("[");
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineEvent(");
			regJS.append(event.getX()).append(", ");
			regJS.append("'").append(getEventMarkElementId(context, i)).append("', ");
			regJS.append("'").append(getEventTextElementId(context, i)).append("'");
			regJS.append(")");
		}
		regJS.append("], ");
		
		// JS graphs
		regJS.append("[");
		for (int i = 0; i < graphs.length; i++)
		{
			EventLineGraph graph = graphs[i];
			int x[] = graph.getX();
			double y[] = graph.getY();
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineGraph(");
			regJS.append("'").append(JsfUtils.escapeStringForJS(graph.getName())).append("', ");
			regJS.append("'").append(graph.getColor()).append("', ");
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
			regJS.append("]");
			regJS.append(")");
		}
		regJS.append("], ");
		
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
		regJS.append("], ");

		// vertical labels
		regJS.append(verticalLabels.getSpacing()).append(", ");
		regJS.append(verticalLabels.getMajorSpacing()).append(", ");

		// JS events
		regJS.append("[");
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new EventLineEvent(");
			regJS.append(event.getX()).append(", ");
			regJS.append("'").append(JsfUtils.escapeStringForJS(event.getText())).append("'");
			regJS.append(")");
		}
		regJS.append("]");
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
			"position: relative;";

		// main div container
		writer.startElement("div", this);
		writer.writeAttribute("id", mainId, null);
		writer.writeAttribute("style", mainContainerStyle, null);
		writer.writeAttribute("class", "event-line-container", null);
		
		// graphs
		encodeGraphsContainer(writer, graphsContainerId, maxSlots);

		// selector
		encodeSelector(writer, selectorContainerId, selectorId, leftSelectorId, rightSelectorId, maxSlots);
		
		// indicator
		encodeIndicator(writer, indicatorContainerId, indicatorId, indicatorLabelId);

		// end of the main container
		writer.endElement("div");

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

	public EventLineVerticalLabels getVerticalLabels()
	{
		return (EventLineVerticalLabels) JsfUtils.getCompPropObject(this, getFacesContext(),
				"verticalLabels", verticalLabelsSet, verticalLabels);
	}

	public void setVerticalLabels(EventLineVerticalLabels verticalLabels)
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

	public int getViewportHeight()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"viewportHeight", viewportHeightSet, viewportHeight);
	}

	public void setViewportHeight(int viewportHeight)
	{
		viewportHeightSet = true;
		this.viewportHeight = viewportHeight;
	}

}