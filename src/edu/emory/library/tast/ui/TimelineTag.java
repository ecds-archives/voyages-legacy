package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class TimelineTag extends UIComponentTag
{
	
	private String markers;
	private String markerWidth;
	private String leftExtent;
	private String rightExtent;
	
	protected void setProperties(UIComponent component)
	{
		
		TimelineComponent timeline = (TimelineComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (markers != null && isValueReference(markers))
		{
			ValueBinding vb = app.createValueBinding(markers);
			timeline.setValueBinding("markers", vb);
		}
		else
		{
			timeline.setMarkers(markers.split(","));
		}
		
		if (markerWidth != null && isValueReference(markerWidth))
		{
			ValueBinding vb = app.createValueBinding(markerWidth);
			timeline.setValueBinding("markerWidth", vb);
		}
		else
		{
			timeline.setMarkerWidth(Integer.parseInt(markerWidth));
		}

		if (leftExtent != null && isValueReference(leftExtent))
		{
			ValueBinding vb = app.createValueBinding(leftExtent);
			timeline.setValueBinding("leftExtent", vb);
		}
		else
		{
			timeline.setLeftExtent(Integer.parseInt(leftExtent));
		}

		if (rightExtent != null && isValueReference(rightExtent))
		{
			ValueBinding vb = app.createValueBinding(rightExtent);
			timeline.setValueBinding("rightExtent", vb);
		}
		else
		{
			timeline.setRightExtent(Integer.parseInt(rightExtent));
		}

	}
	
	public String getComponentType()
	{
		return "Timeline";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getMarkerWidth()
	{
		return markerWidth;
	}

	public void setMarkerWidth(String markerWidth)
	{
		this.markerWidth = markerWidth;
	}

	public String getLeftExtent()
	{
		return leftExtent;
	}

	public void setLeftExtent(String leftExtent)
	{
		this.leftExtent = leftExtent;
	}

	public String getRightExtent()
	{
		return rightExtent;
	}

	public void setRightExtent(String rightExtent)
	{
		this.rightExtent = rightExtent;
	}

	public String getMarkers()
	{
		return markers;
	}

	public void setMarkers(String markers)
	{
		this.markers = markers;
	}

}
