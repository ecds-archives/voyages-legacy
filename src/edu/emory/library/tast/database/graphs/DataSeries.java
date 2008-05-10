package edu.emory.library.tast.database.graphs;


public class DataSeries
{
	
	private DependentVariable variable;
	public int aggregate;

	public DataSeries(DependentVariable variable, int aggregate)
	{
		this.variable = variable;
		this.aggregate = aggregate;
	}

	public DependentVariable getVariable()
	{
		return variable;
	}

	public int getAggregate()
	{
		return aggregate;
	}

	public String formatForDisplay()
	{
		return variable.formatVariableForDisplay(aggregate);
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
		if (aggregate != other.aggregate)
			return false;
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