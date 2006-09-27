package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class EventLineTag extends UIComponentTag
{
	
	private String graphHeight;
	private String barWidth;
	private String graphs;
	private String events;
	private String horizontalLabels;
	private String verticalLabels;

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
		
		if (barWidth != null && isValueReference(barWidth))
		{
			ValueBinding vb = app.createValueBinding(barWidth);
			eventLine.setValueBinding("barWidth", vb);
		}
		else
		{
			try
			{
				eventLine.setBarWidth(Integer.parseInt(barWidth));
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

		if (horizontalLabels != null && isValueReference(horizontalLabels))
		{
			ValueBinding vb = app.createValueBinding(horizontalLabels);
			eventLine.setValueBinding("horizontalLabels", vb);
		}

		if (verticalLabels != null && isValueReference(verticalLabels))
		{
			ValueBinding vb = app.createValueBinding(verticalLabels);
			eventLine.setValueBinding("verticalLabels", vb);
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

	public String getBarWidth()
	{
		return barWidth;
	}

	public void setBarWidth(String width)
	{
		this.barWidth = width;
	}

	public String getEvents()
	{
		return events;
	}

	public void setEvents(String items)
	{
		this.events = items;
	}

	public String getHorizontalLabels()
	{
		return horizontalLabels;
	}

	public void setHorizontalLabels(String horizontalLabels)
	{
		this.horizontalLabels = horizontalLabels;
	}

	public String getVerticalLabels()
	{
		return verticalLabels;
	}

	public void setVerticalLabels(String verticalLabels)
	{
		this.verticalLabels = verticalLabels;
	}

}
