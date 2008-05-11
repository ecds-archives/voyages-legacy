package edu.emory.library.tast.database.graphs;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;

public class DependentVariable
{
	
	public static final int COUNT = 1; 
	public static final int SUM = 2; 
	public static final int MIN = 4; 
	public static final int MAX = 16; 
	public static final int AVG = 32; 
	public static final int INVALID_AGGREGATE = 1024; 
	
	private String id;
	private String label;
	private Attribute selectAttribute;
	private DependentVariableTransformer transformer;
	private int aggregate;
	
	private DependentVariable(String id, String label, Attribute selectAttribute, DependentVariableTransformer transformer, int aggregate)
	{
		this.id = id;
		this.selectAttribute = selectAttribute;
		this.transformer = transformer;
		this.label = label;
		this.aggregate = aggregate;
	}
	
	public static DependentVariable createAvg(String id, String label, String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerSimple(), AVG);
	}

	public static DependentVariable createSum(String id, String label, String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerSimple(), SUM);
	}

	public static DependentVariable createCount(String id, String label,  String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerSimple(), COUNT);
	}

	public static DependentVariable createPercentage(String id, String label, String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerPercentage(), AVG);
	}

	public static int parseAggregateType(String aggregate, boolean lenient)
	{
		if (aggregate != null)
		{
			if (aggregate.equals("count"))
				return DependentVariable.COUNT;
			else if (aggregate.equals("min"))
				return DependentVariable.MIN;
			else if (aggregate.equals("max"))
				return DependentVariable.MAX;
			else if (aggregate.equals("avg"))
				return DependentVariable.AVG;
			else if (aggregate.equals("sum"))
				return DependentVariable.SUM;
		}
		if (lenient)
			return INVALID_AGGREGATE;
		else
			throw new RuntimeException("invalid aggregate '" + aggregate + "'");
	}
	
	public static String aggregateToHQL(int aggregate)
	{
		return aggregateToString(aggregate);
	}

	public static String aggregateToString(int aggregate)
	{
		switch (aggregate)
		{
		case COUNT:
			return "count";
		case SUM:
			return "sum";
		case MIN:
			return "min";
		case MAX:
			return "max";
		case AVG:
			return "avg";
		default:
			throw new RuntimeException("invalid aggregate " + aggregate);
		}
	}

	public String getId()
	{
		return id;
	}
	
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DependentVariable other = (DependentVariable) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getLabel()
	{
		return label;
	}
	
	public Attribute getSelectAttribute()
	{
		return selectAttribute;
	}
	
	public DependentVariableTransformer getTransformer()
	{
		return transformer;
	}

	public int getAggregate()
	{
		return aggregate;
	}
	
}

abstract class DependentVariableTransformer
{
	public abstract Number transform(Object value);
}

class DependentVariableTransformerSimple extends DependentVariableTransformer 
{
	public Number transform(Object value)
	{
		return (Number) value;
	}
}

class DependentVariableTransformerPercentage extends DependentVariableTransformer 
{
	public Number transform(Object value)
	{
		return new Double(((Number) value).doubleValue() * 100);
	}
}