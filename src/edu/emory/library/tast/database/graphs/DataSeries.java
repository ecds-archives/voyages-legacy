package edu.emory.library.tast.database.graphs;


public class DataSeries
{
	
	private DependentVariable variable;
	public String aggregate;

	public DataSeries(DependentVariable variable, String aggregate)
	{
		this.variable = variable;
		this.aggregate = aggregate;
	}

	public DependentVariable getVariable()
	{
		return variable;
	}

	public String getAggregate()
	{
		return aggregate;
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
		if (aggregate == null)
		{
			if (other.aggregate != null)
				return false;
		}
		else if (!aggregate.equals(other.aggregate))
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