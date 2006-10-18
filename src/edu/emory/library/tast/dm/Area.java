package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Area
{
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", "Area"));
		attributes.put("name", new StringAttribute("name", "Area"));
		attributes.put("america", new NumericAttribute("america", "Area"));
	}
	
	private int id;
	private String name;
	private boolean america;

	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isAmerica()
	{
		return america;
	}

	public void setAmerica(boolean america)
	{
		this.america = america;
	}

	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}

}
