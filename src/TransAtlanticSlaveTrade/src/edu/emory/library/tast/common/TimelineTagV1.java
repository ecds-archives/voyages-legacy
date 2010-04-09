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

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class TimelineTagV1 extends UIComponentTag
{
	
	private String markers;
	private String markerWidth;
	private String leftExtent;
	private String rightExtent;
	
	protected void setProperties(UIComponent component)
	{
		
		TimelineComponentV1 timeline = (TimelineComponentV1) component;
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
