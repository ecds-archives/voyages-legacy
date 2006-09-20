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
	private String dataSequences;
	private String items;

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

		if (dataSequences != null && isValueReference(dataSequences))
		{
			ValueBinding vb = app.createValueBinding(dataSequences);
			eventLine.setValueBinding("dataSequences", vb);
		}

		if (items != null && isValueReference(items))
		{
			ValueBinding vb = app.createValueBinding(items);
			eventLine.setValueBinding("items", vb);
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

	public String getDataSequences()
	{
		return dataSequences;
	}

	public void setDataSequences(String graphs)
	{
		this.dataSequences = graphs;
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

	public String getItems()
	{
		return items;
	}

	public void setItems(String items)
	{
		this.items = items;
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
