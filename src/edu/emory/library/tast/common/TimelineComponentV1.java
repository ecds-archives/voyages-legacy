package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class TimelineComponentV1 extends UIComponentBase
{
	
	private boolean markersSet = false;
	private String[] markers;

	private boolean markWidthSet = false;
	private int markerWidth;

	private boolean leftExtentSet = false;
	private int leftExtent;

	private boolean rightExtentSet = false;
	private int rightExtent;
	
	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context); 
		values[1] = markers; 
		values[2] = new Integer(markerWidth); 
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		markers = (String[]) values[1];
		markerWidth = ((Integer) values[2]).intValue(); 
	}
	
	private String getFieldNameForLeftExtent(FacesContext context)
	{
		return getClientId(context) + "_left";
	}
	
	private String getFieldNameForRightExtent(FacesContext context)
	{
		return getClientId(context) + "_right";
	}
	
	public void decode(FacesContext context)
	{

		Map params = context.getExternalContext().getRequestParameterMap();
		
		leftExtent = JsfUtils.getParamInt(params, getFieldNameForLeftExtent(context), 0);
		rightExtent = JsfUtils.getParamInt(params, getFieldNameForRightExtent(context), 0);
		
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// pull data from beans
		String[] markers = getMarkers();
		int markerWidth = getMarkerWidth();
		int totalWidth = markerWidth * markers.length;
		int leftExtent = getLeftExtent();
		int rightExtent = getRightExtent();
		
		// hidden field for left extent
		JsfUtils.encodeHiddenInput(this, writer,
				getFieldNameForLeftExtent(context),
				String.valueOf(leftExtent));
		
		// hidden field for right extent
		JsfUtils.encodeHiddenInput(this, writer,
				getFieldNameForRightExtent(context),
				String.valueOf(rightExtent));

		// element names
		String sliderContainerElementId = getClientId(context) + "_slider_container";
		String knobLeftElementId = getClientId(context) + "_knob_left";
		String knobRightElementId = getClientId(context) + "_knob_right";
		String selectionElementId = getClientId(context) + "_selection";
		
		// registration script
		StringBuffer regJS = new StringBuffer();
		regJS.append("TimelineGlobals.registerTimeline(new Timeline(");
		regJS.append("'").append(getClientId(context)).append("', ");
		regJS.append("'").append(form.getClientId(context)).append("', ");
		regJS.append(markerWidth).append(", ");
		regJS.append(markers.length).append(", ");
		regJS.append("'").append(getFieldNameForLeftExtent(context)).append("', ");
		regJS.append("'").append(getFieldNameForRightExtent(context)).append("', ");
		regJS.append("'").append(sliderContainerElementId).append("', ");
		regJS.append("'").append(knobLeftElementId).append("', ");
		regJS.append("'").append(knobRightElementId).append("', ");
		regJS.append("'").append(selectionElementId).append("'));");
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
		// slider container
		writer.startElement("div", this);
		writer.writeAttribute("id", sliderContainerElementId, null);
		writer.writeAttribute("class", "timetime-slider-container", null);
		writer.writeAttribute("style", "width: " + totalWidth + "px", null);
		
		// left knob
		writer.startElement("div", this);
		writer.writeAttribute("id", knobLeftElementId, null);
		writer.writeAttribute("class", "timetime-left-knob", null);
		writer.endElement("div");
		
		// center span
		writer.startElement("div", this);
		writer.writeAttribute("id", selectionElementId, null);
		writer.writeAttribute("class", "timetime-selection", null);
		writer.endElement("div");

		// right knob
		writer.startElement("div", this);
		writer.writeAttribute("id", knobRightElementId, null);
		writer.writeAttribute("class", "timetime-right-knob", null);
		writer.endElement("div");

		// end slider container
		writer.endElement("div");
		
		// begin markers
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "timeline-markers", null);
		writer.startElement("tr", this);
		
		// markers
		for (int i = 0; i < markers.length; i++)
		{
			writer.startElement("td", this);
			writer.writeAttribute("style", "width: " + markerWidth + "px", null);
			writer.write(markers[i]);
			writer.endElement("td");
		}
		
		// end markers
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	public void encodeChildren(FacesContext arg0) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext arg0) throws IOException
	{
	}

	public void setMarkers(String[] markers)
	{
		markersSet = true;
		this.markers = markers;
	}

	public String[] getMarkers()
	{
        if (markersSet) return markers;
        ValueBinding vb = getValueBinding("markers");
        if (vb == null) return markers;
        return (String[]) vb.getValue(getFacesContext());
	}

	public void setMarkerWidth(int markWidth)
	{
		markWidthSet = true;
		this.markerWidth = markWidth;
	}

	public int getMarkerWidth()
	{
        if (markWidthSet) return markerWidth;
        ValueBinding vb = getValueBinding("markWidth");
        if (vb == null) return markerWidth;
        return ((Integer) vb.getValue(getFacesContext())).intValue();
	}

	public void setLeftExtent(int leftExtent)
	{
		leftExtentSet = true;
		this.leftExtent = leftExtent;
	}

	public int getLeftExtent()
	{
        if (leftExtentSet) return leftExtent;
        ValueBinding vb = getValueBinding("leftExtent");
        if (vb == null) return leftExtent;
        return ((Integer) vb.getValue(getFacesContext())).intValue();
	}

	public void setRightExtent(int rightExtent)
	{
		rightExtentSet = true;
		this.rightExtent = rightExtent;
	}

	public int getRightExtent()
	{
        if (rightExtentSet) return rightExtent;
        ValueBinding vb = getValueBinding("rightExtent");
        if (vb == null) return rightExtent;
        return ((Integer) vb.getValue(getFacesContext())).intValue();
	}

}
