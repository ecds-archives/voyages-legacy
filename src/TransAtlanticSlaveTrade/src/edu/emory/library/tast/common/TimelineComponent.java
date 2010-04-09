/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class TimelineComponent extends UIComponentBase
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
	
	public void processUpdates(FacesContext context)
	{
		
		ValueBinding vbLeftExtent = getValueBinding("leftExtent");
		if (vbLeftExtent != null) vbLeftExtent.setValue(context, new Integer(leftExtent));
	
		ValueBinding vbRightExtent = getValueBinding("rightExtent");
		if (vbRightExtent != null) vbRightExtent.setValue(context, new Integer(rightExtent));
		
	}
	
	private int cropExtent(int extent, int min, int max)
	{
		if (extent < min)
			return extent;
		else if (extent > max)
			return max;
		else
			return extent;
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// pull data from beans
		String[] markers = getMarkers();
		int markerWidth = getMarkerWidth();
		int leftExtent = getLeftExtent();
		int rightExtent = getRightExtent();
		
		// check range
		leftExtent = cropExtent(leftExtent, 0, markers.length - 2);
		rightExtent = cropExtent(rightExtent, 1, markers.length - 1);
		if (!(leftExtent < rightExtent)) rightExtent = leftExtent + 1;
		
		// hidden field for left extent
		JsfUtils.encodeHiddenInput(this, writer,
				getFieldNameForLeftExtent(context),
				String.valueOf(leftExtent));
		
		// hidden field for right extent
		JsfUtils.encodeHiddenInput(this, writer,
				getFieldNameForRightExtent(context),
				String.valueOf(rightExtent));

		// registration script
		StringBuffer regJS = new StringBuffer();
		regJS.append("TimelineGlobals.registerTimeline(new Timeline(");
		regJS.append("'").append(getClientId(context)).append("', ");
		regJS.append("'").append(form.getClientId(context)).append("', ");
		regJS.append(markerWidth).append(", ");
		regJS.append(markers.length).append(", ");
		regJS.append("'").append(getFieldNameForLeftExtent(context)).append("', ");
		regJS.append("'").append(getFieldNameForRightExtent(context)).append("'");
		regJS.append("))");
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
		// begin markers
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "timeline", null);
		writer.writeAttribute("id", getClientId(context), null);
		writer.startElement("tr", this);
		
		// markers
		for (int i = 0; i < markers.length; i++)
		{

			String className = null;
			if (0 < i && i < markers.length-1)
			{
				if (leftExtent < i && i < rightExtent)
					className = "timeline-inside";
				else if (i < leftExtent || rightExtent < i)
					className = "timeline-outside";
				else if (i == leftExtent)
					className = "timeline-left-marker";
				else
					className = "timeline-right-marker";
			}
			else if (i == 0)
			{
				if (leftExtent == 0)
					className = "timeline-left-boundary-marker";
				else
					className = "timeline-left-boundary";
			}
			else
			{
				if (rightExtent == markers.length-1)
					className = "timeline-right-boundary-marker";
				else
					className = "timeline-right-boundary";
			}

			writer.startElement("td", this);
			writer.writeAttribute("style", "width: " + markerWidth + "px", null);
			writer.writeAttribute("class", className, null);
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
