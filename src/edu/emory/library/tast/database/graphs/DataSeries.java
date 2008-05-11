package edu.emory.library.tast.database.graphs;


public class DataSeries
{
	
	private DependentVariable variable;

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