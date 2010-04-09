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
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.emory.library.tast.TastResource;

public class GraphTypeBar extends GraphType
{

	protected GraphTypeBar(String id, IndependentVariable[] independentVariables, DependentVariable[] dependentVariables)
	{
		super(id, independentVariables, dependentVariables);
	}

	public boolean canHaveMoreSeries()
	{
		return true;
	}

	public JFreeChart createChart()
	{
		return null;
	}

	public JFreeChart createChart(Object[] data)
	{
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		JFreeChart chart = ChartFactory.createBarChart(
				null,
				getSelectedIndependentVariable().getLabel(),
				TastResource.getText("components_charts_barvalue"),
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
		
		CategoryPlot plot = chart.getCategoryPlot();
		plot.getDomainAxis().setMaximumCategoryLabelLines(5);
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
		chart.setBackgroundPaint(Color.white);
		
		Format formatter = getSelectedIndependentVariable().getFormat();
		
		List allDataSeries = getDataSeries();
		for (int j = 0; j < allDataSeries.size(); j++)
		{
			
			DataSeries dataSearies = (DataSeries) allDataSeries.get(j);
			String dataSeriesLabel = dataSearies.formatForDisplay();
			
			for (int i = 0; i < data.length; i++)
			{
				Object [] row = (Object[])data[i];
				
				String cat = formatter == null ?
						row[0].toString() :
							formatter.format(row[0]);
						
				dataset.addValue(
						(Number) row[j+1],
						dataSeriesLabel,
						cat);
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
