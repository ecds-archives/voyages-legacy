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
	private int agregates;
	
	private DependentVariable(String id, String label, Attribute selectAttribute, DependentVariableTransformer transformer, int agregates)
	{
		this.id = id;
		this.selectAttribute = selectAttribute;
		this.transformer = transformer;
		this.agregates = agregates;
		this.label = label;
	}
	
	public static DependentVariable createStandard(String id, String label, String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerSimple(), SUM + MIN + MAX + AVG);
	}

	public static DependentVariable createCounter(String id, String label,  String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerSimple(), COUNT);
	}

	public static DependentVariable createPercentage(String id, String label, String name)
	{
		Attribute attr = Voyage.getAttribute(name);
		return new DependentVariable(id, label, attr, new DependentVariableTransformerPercentage(), MIN + MAX + AVG);
	}

	public String formatVariableForDisplay(int aggregate)
	{
		switch (aggregate)
		{
		case COUNT:
			return label;
		case SUM:
			return "Sum of " + label;
		case MIN:
			return "Minimum of " + label;
		case MAX:
			return "Maximum of " + label;
		case AVG:
			return "Average of " + label;
		default:
			throw new RuntimeException("invalid aggregate " + aggregate);
		}
	}
	
	public static int parseAggregateType(String aggregate)
	{
		return parseAggregateType(aggregate, false);
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

	public boolean hasOnlyCount()
	{
		return agregates == COUNT;
	}

	public boolean hasCount()
	{
		return (agregates & COUNT) != 0;
	}

	public boolean hasMin()
	{
		return (agregates & MIN) != 0;
	}

	public boolean hasMax()
	{
		return (agregates & MAX) != 0;
	}

	public boolean hasAvg()
	{
		return (agregates & AVG) != 0;
	}

	public boolean hasSum()
	{
		return (agregates & SUM) != 0;
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