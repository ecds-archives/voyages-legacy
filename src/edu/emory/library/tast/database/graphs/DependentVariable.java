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