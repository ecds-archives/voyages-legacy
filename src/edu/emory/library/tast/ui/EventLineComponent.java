package edu.emory.library.tast.ui;

import java.awt.BasicStroke;
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
	private static final int MARK_WIDTH = 11;
	private static final int MARK_HEIGHT = 11;
	
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
	
	/*
	private String renderGraph(EventLineGraph graph, int width, int height, int barWidth, int barOffset, int innerBarWidth) throws IOException
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
					i*barWidth + barOffset,
					height - graphValues[i],
					innerBarWidth,
					graphValues[i]);
		
		gr.dispose();
		
		String fileName = new UidGenerator().generate() + ".png";
		File imageFile = new File(AppConfig.getConfiguration().getString(AppConfig.EVENTLINE_IMAGE_DIR), fileName);
		
		// ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		ImageIO.write(image, "png", imageFile);
		// byte[] imageBytes = imageStream.toByteArray();
		
		return fileName;
		
	}
	*/
	
	
	private String createImage(EventLineGraph[] graphs, EventLineEvent[] events, EventLineHorizontalLabels horizontalLabels, int width, int height, int barWidth, int barOffset, int innerBarWidth) throws IOException
	{

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = image.createGraphics();
		
		gr.setBackground(new Color(0,0,0,0));
		gr.clearRect(0, 0, width, height);
		
		for (int i = 0; i < graphs.length; i++)
		{
			EventLineGraph graph = graphs[i];
			
			gr.setColor(graph.getColor());

			int n = graph.getCount();
			EventLineDataPoint[] data = graph.getData();
			
			for (int j = 0; j < n; j++)
			{
				int value = data[j].getValue();
				gr.fillRect(
						j*barWidth + barOffset,
						height - value,
						innerBarWidth,
						value);
			}
		
		}
		
		gr.setColor(Color.BLACK);
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			int x = event.getPosition() * barWidth + barOffset + innerBarWidth / 2;
			gr.drawLine(x, 0, x, height);
		}
		
		gr.setStroke(new BasicStroke(
				1.0f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				10.0f,
				new float[]{2.0f},
				0.0f));

		for (int i = 0; i < horizontalLabels.getCount(); i++)
		{
			int x = (horizontalLabels.getStart() + i * horizontalLabels.getSpace()) * barWidth + barOffset + innerBarWidth / 2;
			gr.drawLine(x, 0, x, height);
		}

		gr.setStroke(new BasicStroke());
		gr.drawLine(0, 0, 0, height);
		gr.drawLine(0, height-1, width, height-1);

		String fileName = new UidGenerator().generate() + ".png";
		File imageFile = new File(AppConfig.getConfiguration().getString(AppConfig.EVENTLINE_IMAGE_DIR), fileName);
		
		ImageIO.write(image, "png", imageFile);

		return fileName;
		
	}

	private String getEventMarkElementId(FacesContext context, int index)
	{
		return getClientId(context) + "_event_mark_" + index;
	}

	private void encodeEventMarks(FacesContext context, ResponseWriter writer, String mainId, EventLineEvent[] events, int barOffset, int innerBarWidth) throws IOException
	{
		// marks for events
		StringBuffer markCssStyle = new StringBuffer();
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];

			// position
			int x = event.getPosition() * barWidth + LEFT_LABELS_WIDTH + LABELS_MARGIN +
				barOffset + innerBarWidth / 2 - (MARK_WIDTH - 1) / 2;

			// CSS
			markCssStyle.setLength(0);
			markCssStyle.append("position: absolute; ");
			markCssStyle.append("left: ").append(x).append("px; ");
			markCssStyle.append("top: ").append(TOP_LABELS_HEIGHT + LABELS_MARGIN + graphHeight).append("px; ");
			markCssStyle.append("width: ").append(MARK_WIDTH).append("px; ");
			markCssStyle.append("height: ").append(MARK_HEIGHT).append("px; ");
			
			// onclick
			String onclick = 
				"EventLineGlobals.toggleEvent(" +
				"'" + mainId + "'," +
				+ i + ")";
			
			// image 
			writer.startElement("div", this);
			writer.writeAttribute("id", getEventMarkElementId(context, i), null);
			writer.writeAttribute("style", markCssStyle, null);
			writer.writeAttribute("class", "event-line-mark", null);
			writer.writeAttribute("onclick", onclick, null);
			writer.endElement("div");
			
		}
	}
	
	private void encodeIndicator(ResponseWriter writer, String indicatorContainerId, String indicatorId, String indicatorLabelId, int graphWidth, int totalHeight) throws IOException
	{
		// style of the indicator container
		StringBuffer indicatorContainerCssSyle = new StringBuffer();
		indicatorContainerCssSyle.append("position: absolute; ");
		indicatorContainerCssSyle.append("left: ").append(LEFT_LABELS_WIDTH + LABELS_MARGIN).append("px; ");
		indicatorContainerCssSyle.append("top: 0px; ");
		indicatorContainerCssSyle.append("width: ").append(graphWidth).append("px; ");
		indicatorContainerCssSyle.append("height: ").append(totalHeight).append("px;");

		// indicator container
		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorContainerId, null);
		writer.writeAttribute("style", indicatorContainerCssSyle, null);
		writer.writeAttribute("class", "event-line-indicator-container", null);
		
		// style of the indicator
		StringBuffer indicatorCssSyle = new StringBuffer();
		indicatorCssSyle.append("position: absolute; ");
		indicatorCssSyle.append("display: none; ");
		indicatorCssSyle.append("width: ").append(barWidth).append("px; ");
		indicatorCssSyle.append("height: ").append(totalHeight).append("px;");

		// div line indicator
		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorId, null);
		writer.writeAttribute("style", indicatorCssSyle, null);
		writer.writeAttribute("class", "event-line-indicator", null);
		writer.endElement("div");

		writer.startElement("div", this);
		writer.writeAttribute("id", indicatorLabelId, null);
		writer.writeAttribute("style", "display: none; position: absolute; top: " + totalHeight + "px", null);
		writer.writeAttribute("class", "event-line-indicator-label", null);
		writer.endElement("div");

		// indicator container
		writer.endElement("div");
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
	
	private void encodeImage(ResponseWriter writer, int graphWidth, String imageUrl) throws IOException
	{
		
		StringBuffer imageCssStyle = new StringBuffer();
		imageCssStyle.append("position: absolute; ");
		imageCssStyle.append("left: ").append(LEFT_LABELS_WIDTH + LABELS_MARGIN).append("px; ");
		imageCssStyle.append("top: ").append(TOP_LABELS_HEIGHT + LABELS_MARGIN).append("px; ");
		imageCssStyle.append("width: ").append(graphWidth).append("px; ");
		imageCssStyle.append("height: ").append(graphHeight).append("px; ");
		imageCssStyle.append("background-image: url(").append(imageUrl).append("); ");
		
		writer.startElement("div", this);
		writer.writeAttribute("style", imageCssStyle, null);
		writer.writeAttribute("class", "event-line-image", null);
		writer.endElement("div");

	}
	
	private void encodeHorozontalLabels(ResponseWriter writer, EventLineVerticalLabels verticalLabels, int totalHeight) throws IOException
	{
		for (int i = 0; i < verticalLabels.getCount(); i++)
		{
			
			int top =
				totalHeight -
				(verticalLabels.getStart() + i * verticalLabels.getSpace()) -  
				LEFT_LABELS_HEIGHT / 2; 

			encodeLabel(writer, verticalLabels.getLabel(i), "event-line-left-label",
					0, top, LEFT_LABELS_WIDTH, LEFT_LABELS_HEIGHT, "right", "center");
			
		}
	}

	private void encodeVerticalLabels(ResponseWriter writer, EventLineHorizontalLabels horizontalLabels) throws IOException
	{
		for (int i = 0; i < horizontalLabels.getCount(); i++)
		{
			
			int left =
				(horizontalLabels.getStart() + i * horizontalLabels.getSpace()) * barWidth +
				barWidth / 2 +
				LEFT_LABELS_WIDTH -
				TOP_LABELS_WIDTH / 2 +
				LABELS_MARGIN;
			
			encodeLabel(writer, horizontalLabels.getLabel(i), "event-line-top-label",
					left, 0, TOP_LABELS_WIDTH, TOP_LABELS_HEIGHT, "center", "bottom");

		}
	}
	
	private String getEventTextElementId(FacesContext context, int index)
	{
		return getClientId(context) + "_event_text_" + index;
	}
	
	private void encodeEvents(FacesContext context, ResponseWriter writer, int graphWidth, EventLineEvent[] events) throws IOException
	{
		
		StringBuffer imageCssStyle = new StringBuffer();
		imageCssStyle.append("display: none; ");
		imageCssStyle.append("margin-left: ").append(LEFT_LABELS_WIDTH + LABELS_MARGIN).append("px; ");
		imageCssStyle.append("width: ").append(graphWidth).append("px;");
		String imageCssStyleStr = imageCssStyle.toString();
		
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			writer.startElement("div", this);
			writer.writeAttribute("id", getEventTextElementId(context, i), null);
			writer.writeAttribute("style", imageCssStyleStr, null);
			writer.write(event.getText());
			writer.endElement("div");
		}

	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		String contextPath = context.getExternalContext().getRequestContextPath();
//		UIForm form = JsfUtils.getForm(this, context);

		// HTML ids
		String mainId = getClientId(context);
		String indicatorContainerId = getClientId(context) + "_indicator_contaiter";
		String indicatorId = getClientId(context) + "_indicator";
		String indicatorLabelId = getClientId(context) + "_indicator_label";

		// these data won't be saved in the component state
		barWidth = getBarWidth();
		graphHeight = getGraphHeight();
		EventLineGraph[] graphs = getGraphs();
		EventLineEvent[] events = getEvents();
		EventLineHorizontalLabels horizontalLabels = getHorizontalLabels();
		EventLineVerticalLabels verticalLabels = getVerticalLabels();
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
		
		// space between bars
		int barOffset;
		int innerBarWidth;
		if (barWidth == 1)
		{
			barOffset = 0;
			innerBarWidth = barWidth;
		}
		else
		{
			barOffset = 0;
			innerBarWidth = barWidth - 1;
		}
		
		// render the graphs and save them to disk
		String imageName = createImage(graphs, events, horizontalLabels, graphWidth, graphHeight, barWidth, barOffset, innerBarWidth);
		
		// JS registration
		StringBuffer eventsJS = new StringBuffer();
		eventsJS.append("EventLineGlobals.registerEventLine(new EventLine(");
		eventsJS.append("'").append(mainId).append("', ");
		eventsJS.append("'").append(indicatorContainerId).append("', ");
		eventsJS.append("'").append(indicatorId).append("', ");
		eventsJS.append("'").append(indicatorLabelId).append("', ");
		eventsJS.append(barWidth).append(", ");
		eventsJS.append(graphWidth).append(", ");
		eventsJS.append(graphHeight).append(", ");
		eventsJS.append(LEFT_LABELS_WIDTH + LABELS_MARGIN).append(", ");
		eventsJS.append(TOP_LABELS_HEIGHT + LABELS_MARGIN).append(", ");
		
		// JS events
		eventsJS.append("[");
		for (int i = 0; i < events.length; i++)
		{
			EventLineEvent event = events[i];
			if (i > 0) eventsJS.append(", ");
			eventsJS.append("new EventLineEvent(");
			eventsJS.append(event.getPosition()).append(", ");
			eventsJS.append("'").append(getEventMarkElementId(context, i)).append("', ");
			eventsJS.append("'").append(getEventTextElementId(context, i)).append("'");
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
			EventLineDataPoint data[] = graph.getData();
			for (int j = 0; j < data.length; j++)
			{
				if (j > 0) eventsJS.append(", ");
				eventsJS.append("new EventLineDataPoint(");
				eventsJS.append(data[j].getValue()).append(", ");
				eventsJS.append("'").append(JsfUtils.escapeStringForJS(data[j].getLabel())).append("'");
				eventsJS.append(")");
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
		mainContainerCssSyle.append("height: ").append(totalHeight + MARK_HEIGHT).append("px; ");

		// main div container
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(context), null);
		writer.writeAttribute("style", mainContainerCssSyle, null);
		writer.writeAttribute("class", "event-line-container", null);
		
		// image
		encodeImage(writer, graphWidth, contextPath + "/eventline/" + imageName);
		
		// left labels
		encodeVerticalLabels(writer, horizontalLabels);
		
		// top labels
		encodeHorozontalLabels(writer, verticalLabels, totalHeight);

		// event marks
		encodeEventMarks(context, writer, mainId, events, barOffset, innerBarWidth);

		// indicator
		encodeIndicator(writer, indicatorContainerId, indicatorId, indicatorLabelId, graphWidth, totalHeight);

		// main div container
		writer.endElement("div");
		
		// text of events
		encodeEvents(context, writer, graphWidth, events);

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