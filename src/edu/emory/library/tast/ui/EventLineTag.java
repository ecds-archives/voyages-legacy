package edu.emory.library.tast.ui;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class EventLineTag extends UIComponentTag
{
	
	private String height;
	private String width;
	private String xMin;
	private String xMax;
	private String xSubdiv;
	private String yMin;
	private String yMax;
	private String ySubdiv;
	private String graphs;
	private String events;

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
		
		if (height != null && isValueReference(height))
		{
			ValueBinding vb = app.createValueBinding(height);
			eventLine.setValueBinding("height", vb);
		}
		else
		{
			try
			{
				eventLine.setHeight(Integer.parseInt(height));
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		
		if (width != null && isValueReference(width))
		{
			ValueBinding vb = app.createValueBinding(width);
			eventLine.setValueBinding("width", vb);
		}
		else
		{
			try
			{
				eventLine.setWidth(Integer.parseInt(width));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (xMin != null && isValueReference(xMin))
		{
			ValueBinding vb = app.createValueBinding(xMin);
			eventLine.setValueBinding("xMin", vb);
		}
		else
		{
			try
			{
				eventLine.setXMin(Integer.parseInt(xMin));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (xMax != null && isValueReference(xMax))
		{
			ValueBinding vb = app.createValueBinding(xMax);
			eventLine.setValueBinding("xMax", vb);
		}
		else
		{
			try
			{
				eventLine.setXMax(Integer.parseInt(xMax));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (xSubdiv != null && isValueReference(xSubdiv))
		{
			ValueBinding vb = app.createValueBinding(xSubdiv);
			eventLine.setValueBinding("xSubdiv", vb);
		}
		else
		{
			try
			{
				eventLine.setXSubdiv(Integer.parseInt(xSubdiv));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (yMin != null && isValueReference(yMin))
		{
			ValueBinding vb = app.createValueBinding(yMin);
			eventLine.setValueBinding("yMin", vb);
		}
		else
		{
			try
			{
				eventLine.setYMin(Integer.parseInt(yMin));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (yMax != null && isValueReference(yMax))
		{
			ValueBinding vb = app.createValueBinding(yMax);
			eventLine.setValueBinding("yMax", vb);
		}
		else
		{
			try
			{
				eventLine.setYMax(Integer.parseInt(yMax));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (xSubdiv != null && isValueReference(ySubdiv))
		{
			ValueBinding vb = app.createValueBinding(ySubdiv);
			eventLine.setValueBinding("ySubdiv", vb);
		}
		else
		{
			try
			{
				eventLine.setYSubdiv(Integer.parseInt(ySubdiv));
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

	}

	public String getXMin()
	{
		return xMin;
	}

	public void setxMin(String xMin)
	{
		this.xMin = xMin;
	}

	public String getXMax()
	{
		return xMax;
	}

	public void setxMax(String extentRight)
	{
		this.xMax = extentRight;
	}

	public String getGraphs()
	{
		return graphs;
	}

	public void setGraphs(String graphs)
	{
		this.graphs = graphs;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

	public String getxSubdiv()
	{
		return xSubdiv;
	}

	public void setxSubdiv(String subdivision)
	{
		this.xSubdiv = subdivision;
	}

	public String getWidth()
	{
		return width;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	public String getEvents()
	{
		return events;
	}

	public void setEvents(String items)
	{
		this.events = items;
	}

	public String getYMax()
	{
		return yMax;
	}

	public void setyMax(String max)
	{
		yMax = max;
	}

	public String getYMin()
	{
		return yMin;
	}

	public void setyMin(String min)
	{
		yMin = min;
	}

	public String getYSubdiv()
	{
		return ySubdiv;
	}

	public void setySubdiv(String subdiv)
	{
		ySubdiv = subdiv;
	}

}
