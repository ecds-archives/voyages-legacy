package edu.emory.library.tast.database.graphs;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;

public class DependentVariable
{
	
	private String id;
	private String label;
	private Attribute selectAttribute;
	
	private DependentVariable(String id, String label, Attribute selectAttribute)
	{
		this.id = id;
		this.selectAttribute = selectAttribute;
		this.label = label;
	}
	
	public static DependentVariable createAvg(String id, String label, String name)
	{
		Attribute attr = new FunctionAttribute("avg", Voyage.getAttribute(name));
		return new DependentVariable(id, label, attr);
	}

	public static DependentVariable createSum(String id, String label, String name)
	{
		Attribute attr = new FunctionAttribute("sum", Voyage.getAttribute(name));
		return new DependentVariable(id, label, attr);
	}

	public static DependentVariable createCount(String id, String label,  String name)
	{
		Attribute attr = new FunctionAttribute("count", Voyage.getAttribute(name));
		return new DependentVariable(id, label, attr);
	}

	public static DependentVariable createPercentage(String id, String label, String name)
	{
		Attribute attr = new FunctionAttribute("avg", new FunctionAttribute("crop_to_0_100", Voyage.getAttribute(name)));
		return new DependentVariable(id, label, attr);
	}

	public static DependentVariable createCustomAvg(String id, String label, Attribute selectAttribute)
	{
		Attribute attr = new FunctionAttribute("avg", selectAttribute);
		return new DependentVariable(id, label, attr);
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
	
}