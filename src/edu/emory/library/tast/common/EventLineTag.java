package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class EventLineTag extends UIComponentTag
{
	
	private String viewportHeight;
	private String graphHeight;
	private String graphs;
	private String events;
	private String zoomLevels;
	private String verticalLabels;
	private String zoomLevel;
	private String offset;
	private String selectorOffset;
	private String eventsColumns = "1";

	public String getComponentType()
	{
		return "EventLine";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		EventLineComponent eventLine = (EventLineComponent) component;
		
		if (viewportHeight != null && isValueReference(viewportHeight))
		{
			ValueBinding vb = app.createValueBinding(viewportHeight);
			eventLine.setValueBinding("viewportHeight", vb);
		}
		else
		{
			try
			{
				eventLine.setViewportHeight(Integer.parseInt(viewportHeight));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (graphHeight != null && isValueReference(graphHeight))
		{
			ValueBinding vb = app.createValueBinding(graphHeight);
			eventLine.setValueBinding("graphHeight", vb);
		}
		else
		{
			try
			{
				eventLine.setGraphHeight(Integer.parseInt(graphHeight));
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		
		if (zoomLevel != null && isValueReference(zoomLevel))
		{
			ValueBinding vb = app.createValueBinding(zoomLevel);
			eventLine.setValueBinding("zoomLevel", vb);
		}
		else
		{
			try
			{
				eventLine.setZoomLevel(Integer.parseInt(zoomLevel));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (offset != null && isValueReference(offset))
		{
			ValueBinding vb = app.createValueBinding(offset);
			eventLine.setValueBinding("offset", vb);
		}
		else
		{
			try
			{
				eventLine.setOffset(Integer.parseInt(offset));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (selectorOffset != null && isValueReference(selectorOffset))
		{
			ValueBinding vb = app.createValueBinding(selectorOffset);
			eventLine.setValueBinding("selectorOffset", vb);
		}
		else
		{
			try
			{
				eventLine.setOffset(Integer.parseInt(selectorOffset));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (eventsColumns != null && isValueReference(eventsColumns))
		{
			ValueBinding vb = app.createValueBinding(eventsColumns);
			eventLine.setValueBinding("eventsColumns", vb);
		}
		else
		{
			try
			{
				eventLine.setEventsColumns(Integer.parseInt(eventsColumns));
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		
		if (graphs != null && isValueReference(graphs))
		{
			ValueBinding vb = app.createValueBinding(graphs);
			eventLine.setValueBinding("graphs", vb);
		}

		if (events != null && isValueReference(events))
		{
			ValueBinding vb = app.createValueBinding(events);
			eventLine.setValueBinding("events", vb);
		}

		if (verticalLabels != null && isValueReference(verticalLabels))
		{
			ValueBinding vb = app.createValueBinding(verticalLabels);
			eventLine.setValueBinding("verticalLabels", vb);
		}

		if (zoomLevels != null && isValueReference(zoomLevels))
		{
			ValueBinding vb = app.createValueBinding(zoomLevels);
			eventLine.setValueBinding("zoomLevels", vb);
		}

	}

	public String getGraphs()
	{
		return graphs;
	}

	public void setGraphs(String graphs)
	{
		this.graphs = graphs;
	}

	public String getGraphHeight()
	{
		return graphHeight;
	}

	public void setGraphHeight(String height)
	{
		this.graphHeight = height;
	}

	public String getEvents()
	{
		return events;
	}

	public void setEvents(String items)
	{
		this.events = items;
	}

	public String getVerticalLabels()
	{
		return verticalLabels;
	}

	public void setVerticalLabels(String verticalLabels)
	{
		this.verticalLabels = verticalLabels;
	}

	public String getZoomLevels()
	{
		return zoomLevels;
	}

	public void setZoomLevels(String zoomLevels)
	{
		this.zoomLevels = zoomLevels;
	}

	public String getOffset()
	{
		return offset;
	}

	public void setOffset(String offset)
	{
		this.offset = offset;
	}

	public String getZoomLevel()
	{
		return zoomLevel;
	}

	public void setZoomLevel(String zoomLevel)
	{
		this.zoomLevel = zoomLevel;
	}

	public String getSelectorOffset()
	{
		return selectorOffset;
	}

	public void setSelectorOffset(String selectorOffset)
	{
		this.selectorOffset = selectorOffset;
	}

	public String getViewportHeight()
	{
		return viewportHeight;
	}

	public void setViewportHeight(String viewportHeight)
	{
		this.viewportHeight = viewportHeight;
	}

	public String getEventsColumns()
	{
		return eventsColumns;
	}

	public void setEventsColumns(String eventsColumns)
	{
		this.eventsColumns = eventsColumns;
	}

}