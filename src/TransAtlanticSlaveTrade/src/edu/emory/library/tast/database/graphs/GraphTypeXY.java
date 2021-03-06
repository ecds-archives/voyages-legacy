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
package edu.emory.library.tast.database.graphs;

import java.awt.Color;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.CategoryTableXYDataset;

import edu.emory.library.tast.TastResource;

public class GraphTypeXY extends GraphType
{

	protected GraphTypeXY(String id, IndependentVariable[] independetVariables, DependentVariable[] dependetVariables)
	{
		super(id, independetVariables, dependetVariables);
	}

	public boolean canHaveMoreSeries()
	{
		return true;
	}

	public JFreeChart createChart(Object[] data)
	{

		CategoryTableXYDataset dataset = new CategoryTableXYDataset();
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				null,
				getSelectedIndependentVariable().getLabel(),
				TastResource.getText("components_charts_barvalue"),
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
		
		Format formatter = getSelectedIndependentVariable().getFormat();
		
		XYPlot plot = chart.getXYPlot();
		if (formatter != null)
			((NumberAxis)plot.getDomainAxis()).setNumberFormatOverride(
					(NumberFormat) formatter);
		
		chart.setBackgroundPaint(Color.white);
		
		List allDataSeries = getDataSeries();
		for (int j = 0; j < allDataSeries.size(); j++)
		{
			DataSeries dataSearies = (DataSeries) allDataSeries.get(j);
			String dataSeriesLabel = dataSearies.formatForDisplay();
			for (int i = 0; i < data.length; i++)
			{
				Object [] row = (Object[])data[i];
				Number x = (Number) row[0];
				Number y = (Number) row[j+1];
				if (x != null && y != null)
					dataset.add(
							x.doubleValue(),
							y.doubleValue(),
							dataSeriesLabel);
			}
		}
		
		LegendItemCollection legendItems = chart.getPlot().getLegendItems();
		for (int i = 0; i < legendItems.getItemCount(); i++)
		{
			LegendItem legendItem = legendItems.get(i);
			DataSeries dataSearies = (DataSeries) allDataSeries.get(i);
			if (legendItem.getFillPaint() instanceof Color)
			{
				dataSearies.setColor(((Color)legendItem.getFillPaint()));
			}
		}
		
		return chart;

	}

}
