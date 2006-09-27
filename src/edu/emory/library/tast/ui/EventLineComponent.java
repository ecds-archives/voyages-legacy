package edu.emory.library.tast.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.imageio.ImageIO;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.UidGenerator;

public class EventLineComponent extends UIComponentBase
{
	
	private static final int TOP_LABELS_HEIGHT = 25;
	private static final int TOP_LABELS_WIDTH = 50;
	private static final int LEFT_LABELS_WIDTH = 60;
	private static final int LEFT_LABELS_HEIGHT = 30;
	private static final int LABELS_MARGIN = 10;
	
	private boolean graphHeightSet = false;
	private int graphHeight;

	private boolean barWidthSet = false;
	private int barWidth;

	private boolean horizontalLabelsSet = false;
	private EventLineHorizontalLabels horizontalLabels;

	private boolean verticalLabelsSet = false;
	private EventLineVerticalLabels verticalLabels;

	private boolean graphsSet = false;
	private EventLineGraph[] graphs;
	
	private boolean eventsSet = false;
	private EventLineEvent[] events;
	
	public Object saveState(FacesContext context)
	{
		Object[] state = new Object[3];
		state[0] = super.saveState(context); 
		state[1] = new Integer(barWidth); 
		state[2] = new Integer(graphHeight); 
		return state;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		barWidth = ((Integer) values[1]).intValue();
		graphHeight = ((Integer) values[2]).intValue();
	}

	public String getFamily()
	{
		return null;
	}
	/*
	private String renderGraph(EventLineGraph graph, int width, int height) throws IOException
	{
		
		int n = graph.getCount();
		int[] x = new int[n];
		int[] y = new int[n];
		int[] graphValues = graph.getData();
		
		for (int i = 0; i < n; i++)
		{
			x[i] = (int) (width * (double) i / (double) n);
			y[i] = height - graphValues[i];
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = image.createGraphics();
		gr.setBackground(new Color(0,0,0,0));
		gr.clearRect(0, 0, width, height);
		gr.setColor(graph.getColor());
		gr.drawPolyline(x, y, n);
		gr.dispose();
		
		String fileName = new UidGenerator().generate() + ".png";
		File imageFile = new File(AppConfig.getConfiguration().getString(AppConfig.EVENTLINE_IMAGE_DIR), fileName);
		
		// ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", imageFile);
		// byte[] imageBytes = imageStream.toByteArray();
		
		return fileName;
		
	}
	*/
	
	private String renderGraph(EventLineGraph graph, int width, int height, int barWidth) throws IOException
	{
		
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = image.createGraphics();
		
		gr.setBackground(new Color(0,0,0,0));
		gr.clearRect(0, 0, width, height);
		
		gr.setColor(graph.getColor());

		int n = graph.getCount();
		int[] graphValues = graph.getData();

		for (int i = 0; i < n; i++)
			gr.fillRect(
					i*barWidth,
					height - graphValues[i],
					barWidth,
					graphValues[i]);
		
		gr.dispose();
		
		String fileName = new UidGenerator().generate() + ".png";
		File imageFile = new File(AppConfig.getConfiguration().getString(AppConfig.EVENTLINE_IMAGE_DIR), fileName);
		
		// ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", imageFile);
		// byte[] imageBytes = imageStream.toByteArray();
		
		return fileName;
		
	}

	private void encodeLabel(ResponseWriter writer, String label, String cssClass, int left, int top, int width, int height, String hAlign, String vAlign) throws IOException
	{
		
		StringBuffer cssStyle = new StringBuffer(); 
		cssStyle.append("position: absolute; ");
		cssStyle.append("left: ").append(left).append("px; ");
		cssStyle.append("top: ").append(top).append("px; ");
		cssStyle.append("width: ").append(width).append("px; ");
		cssStyle.append("height: ").append(height).append("px;");

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("style", cssStyle, null);
		writer.writeAttribute("class", cssClass, null);
		writer.startElement("tr", this);

		cssStyle.setLength(0);
		cssStyle.append("text-align: ").append(hAlign).append("; ");
		cssStyle.append("vertical-align: ").append(vAlign).append(";");
		
		writer.startElement("td", this);
		writer.writeAttribute("style", cssStyle, null);
		writer.write(label);
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");

	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		String contextPath = context.getExternalContext().getRequestContextPath();
//		UIForm form = JsfUtils.getForm(this, context);

		// ids
		String indicatorId = getClientId(context) + "_indicator";

		// these data won't be saved in the component state
		EventLineGraph[] graphs = getGraphs();
		EventLineEvent[] events = getEvents();
		EventLineHorizontalLabels horizontalLabels = getHorizontalLabels();
		EventLineVerticalLabels verticalLabels = getVerticalLabels();
		graphHeight = getGraphHeight();
		if (graphs == null) graphs = new EventLineGraph[0];
		if (events == null) events = new EventLineEvent[0];
		
		// determine needed width
		int barsCount = 0;
		for (int i = 0; i < graphs.length; i++)
		{
			EventLineGraph data = graphs[i];
			if (data.getCount() > barsCount) barsCount = data.getCount(); 
		}

		// total size
		int graphWidth = barWidth * barsCount;
		int totalWidth = LEFT_LABELS_WIDTH + LABELS_MARGIN + graphWidth;
		int totalHeight = TOP_LABELS_HEIGHT + LABELS_MARGIN + graphHeight;
		
		// render the graphs and save them to disk
		String[] graphFileNames = new String[graphs.length];
		for (int i = 0; i < graphs.length; i++)
		{
			EventLineGraph graph = graphs[i];
			graphFileNames[i] = renderGraph(graph, graphWidth, graphHeight, barWidth);
		}
		
		// JS registration
		StringBuffer eventsJS = new StringBuffer();
		eventsJS.append("EventLineGlobals.registerEventLine(new EventLine(");
		eventsJS.append("'").append(getClientId(context)).append("', ");
		eventsJS.append("'").append(indicatorId).append("', ");
		eventsJS.append(barWidth).append(", ");
		eventsJS.append(LEFT_LABELS_WIDTH).append(", ");
		eventsJS.append(TOP_LABELS_HEIGHT).append(", ");
		
		// JS events
		eventsJS.append("[");
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			if (i > 0) eventsJS.append(", ");
			eventsJS.append("new EventLineEvent(");
			eventsJS.append(event.getPosition()).append(", ");
			eventsJS.append("'").append(JsfUtils.escapeStringForJS(event.getTitle())).append("', ");
			eventsJS.append("'").append(JsfUtils.escapeStringForJS(event.getText())).append("'");
			eventsJS.append(")");
		}
		eventsJS.append("], ");
		
		// JS graphs
		eventsJS.append("[");
		for (int i = 0; i < graphs.length; i++)
		{
			EventLineGraph graph = graphs[i];
			if (i > 0) eventsJS.append(", ");
			eventsJS.append("new EventLineGraph(");
			eventsJS.append("'").append(JsfUtils.escapeStringForJS(graph.getName())).append("', ");
			eventsJS.append("[");
			int data[] = graph.getData();
			for (int j = 0; j < data.length; j++)
			{
				if (j > 0) eventsJS.append(", ");
				eventsJS.append(data[j]);
			}
			eventsJS.append("]");
			eventsJS.append(")");
		}
		eventsJS.append("]));");

		// render JS
		JsfUtils.encodeJavaScriptBlock(this, writer, eventsJS);
		
		// style of the main div
		StringBuffer mainContainerCssSyle = new StringBuffer();
		mainContainerCssSyle.append("position: relative; ");
		mainContainerCssSyle.append("width: ").append(totalWidth).append("px; ");
		mainContainerCssSyle.append("height: ").append(totalHeight).append("px; ");

		// main div container
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(context), null);
		writer.writeAttribute("style", mainContainerCssSyle, null);
		writer.writeAttribute("class", "event-line-container", null);
		
		// style of the main div
		StringBuffer indicatorCssSyle = new StringBuffer();
		indicatorCssSyle.append("position: absolute; ");
		indicatorCssSyle.append("display: none; ");
		indicatorCssSyle.append("width: ").append(barWidth).append("px; ");
		indicatorCssSyle.append("height: ").append(graphHeight).append("px;");

		// div which indicates the position
		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorId, null);
		writer.writeAttribute("style", indicatorCssSyle, null);
		writer.writeAttribute("class", "event-line-indicator", null);
		writer.endElement("div");

		// graphs
		StringBuffer graphCssStyle = new StringBuffer();
		for (int i = 0; i < graphs.length; i++)
		{
			
			// image location
			String imageUrl = contextPath + "/eventline/" + graphFileNames[i];
			
			// position by CSS and put the image to it
			graphCssStyle.setLength(0);
			graphCssStyle.append("position: absolute; ");
			graphCssStyle.append("left: ").append(LEFT_LABELS_WIDTH + LABELS_MARGIN).append("px; ");
			graphCssStyle.append("top: ").append(TOP_LABELS_HEIGHT + LABELS_MARGIN).append("px; ");
			graphCssStyle.append("width: ").append(graphWidth).append("px; ");
			graphCssStyle.append("height: ").append(graphHeight).append("px; ");
			graphCssStyle.append("background-image: url(").append(imageUrl).append("); ");
			
			// HTML
			writer.startElement("div", this);
			writer.writeAttribute("style", graphCssStyle, null);
			writer.writeAttribute("class", "event-line-graph", null);
			writer.endElement("div");

		}
		
		// left labels
		for (int i = 0; i < horizontalLabels.getCount(); i++)
		{
			
			// position
			int left =
				(horizontalLabels.getStart() + i * horizontalLabels.getSpace()) * barWidth +
				barWidth / 2 +
				LEFT_LABELS_WIDTH -
				TOP_LABELS_WIDTH / 2 +
				LABELS_MARGIN;
			
			// render
			encodeLabel(writer, horizontalLabels.getLabel(i), "event-line-top-label",
					left, 0, TOP_LABELS_WIDTH, TOP_LABELS_HEIGHT, "center", "bottom");

		}
		
		// top labels
		for (int i = 0; i < verticalLabels.getCount(); i++)
		{
			
			// position
			int top =
				totalHeight -
				(verticalLabels.getStart() + i * verticalLabels.getSpace()) -  
				LEFT_LABELS_HEIGHT / 2; 

			// render
			encodeLabel(writer, verticalLabels.getLabel(i), "event-line-left-label",
					0, top, LEFT_LABELS_WIDTH, LEFT_LABELS_HEIGHT, "right", "center");
			
		}
		
		// main div container
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

	public int getBarWidth()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"barWidth", barWidthSet, barWidth);
	}

	public void setBarWidth(int barWidth)
	{
		barWidthSet = true;
		this.barWidth = barWidth;
	}

	public EventLineHorizontalLabels getHorizontalLabels()
	{
		return (EventLineHorizontalLabels) JsfUtils.getCompPropObject(this, getFacesContext(),
				"horizontalLabels", horizontalLabelsSet, horizontalLabels);
	}

	public void setHorizontalLabels(EventLineHorizontalLabels horizontalLabels)
	{
		verticalLabelsSet = true;
		this.horizontalLabels = horizontalLabels;
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


}