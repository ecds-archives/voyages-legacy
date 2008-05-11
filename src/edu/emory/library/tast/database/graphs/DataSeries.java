package edu.emory.library.tast.database.graphs;

import java.awt.Color;

import edu.emory.library.tast.util.HtmlUtils;


public class DataSeries
{
	
	private DependentVariable variable;
	private boolean selected = false;
	private Color color;

	public DataSeries(DependentVariable variable)
	{
		this.variable = variable;
	}

	public DependentVariable getVariable()
	{
		return variable;
	}

	public String formatForDisplay()
	{
		return variable.getLabel();
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public String getCssLegendBoxStyle()
	{
		if (color == null) return "";
		return "background-color: " + HtmlUtils.formatHtmlColor(color);
	}

	public boolean isHasColor()
	{
		return color != null;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DataSeries other = (DataSeries) obj;
		if (variable == null)
		{
			if (other.variable != null)
				return false;
		}
		else if (!variable.equals(other.variable))
			return false;
		return true;
	}

}