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

import java.util.LinkedList;
import java.util.List;

import org.jfree.chart.JFreeChart;

public abstract class GraphType
{
	
	private String id;
	private IndependentVariable[] independentVariables;
	private DependentVariable[] dependentVariables;
	private int selectedIndependentVariableIdx;
	private int selectedDependentVariableIdx;
	private List dataSeries = new LinkedList();
	
	protected GraphType(String id, IndependentVariable[] independentVariables, DependentVariable[] dependentVariables)
	{
		this.id = id;
		this.independentVariables = independentVariables;
		this.dependentVariables = dependentVariables;
		this.selectedIndependentVariableIdx = 0;
		this.selectedDependentVariableIdx = 0;
	}

	public abstract boolean canHaveMoreSeries();
	public abstract JFreeChart createChart(Object[] data);

	public String getId()
	{
		return id;
	}
	
	public IndependentVariable[] getIndependentVariables()
	{
		return independentVariables;
	}
	
	public DependentVariable[] getDependentVariables()
	{
		return dependentVariables;
	}
	
	public void removeAllSeries()
	{
		dataSeries.clear();
	}
	
	public List getDataSeries()
	{
		return dataSeries;
	}

	public boolean hasDataSeries()
	{
		return dataSeries != null && dataSeries.size() != 0;
	}

	public void setSelectedIndependentVariableId(String id)
	{
		if (id != null)
		{
			for (int i = 0; i < independentVariables.length; i++)
			{
				IndependentVariable var = independentVariables[i];
				if (var.getId().equals(id))
				{
					selectedIndependentVariableIdx = i;
					return;
				}
			}
		}
		selectedIndependentVariableIdx = 0;
	}

	public void setSelectedDependentVariableId(String id)
	{
		if (id != null)
		{
			for (int i = 0; i < dependentVariables.length; i++)
			{
				DependentVariable var = dependentVariables[i];
				if (id.equals(var.getId()))
				{
					selectedDependentVariableIdx = i;
					return;
				}
			}
		}
		selectedDependentVariableIdx = 0;
	}

	public String getSelectedIndependentVariableId()
	{
		return getSelectedIndependentVariable().getId();
	}
	
	public String getSelectedDependentVariableId()
	{
		return getSelectedDependentVariable().getId();
	}

	public DependentVariable getSelectedDependentVariable()
	{
		return dependentVariables[selectedDependentVariableIdx];
	}

	public IndependentVariable getSelectedIndependentVariable()
	{
		return independentVariables[selectedIndependentVariableIdx];
	}
	
}