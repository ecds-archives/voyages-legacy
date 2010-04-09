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
import org.jfree.data.general.DefaultPieDataset;

public class GraphPie extends GraphType
{

	protected GraphPie(String id, IndependentVariable[] independentVariables, DependentVariable[] dependentVariables)
	{
		super(id, independentVariables, dependentVariables);
	}

	public boolean canHaveMoreSeries()
	{
		return false;
	}

	public JFreeChart createChart(Object[] data)
	{

		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		JFreeChart chart = ChartFactory.createPieChart(null, pieDataset, false, true, false);
		chart.setBackgroundPaint(Color.white);
		
		List dataSeries = getDataSeries();		
		if (dataSeries.size() == 0)
			return null;
		
		Format formatter = getSelectedIndependentVariable().getFormat();
		
		for (int i = 0; i < data.length; i++)
		{
			Object [] row = (Object[])data[i];
			
			String cat = formatter == null ?
					row[0].toString() :
						formatter.format(row[0]);
			
			pieDataset.setValue(cat, (Number)row[1]);
		}
		
		return chart;

	}

}
