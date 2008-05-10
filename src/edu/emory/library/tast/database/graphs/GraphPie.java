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
		
		Format format = getSelectedIndependentVariable().getFormat();
		
		for (int i = 0; i < data.length; i++)
		{
			Object [] row = (Object[])data[i];
			pieDataset.setValue(format.format(new Object[] {row[0]}), (Number)row[1]);
		}
		
		return chart;

	}

}
