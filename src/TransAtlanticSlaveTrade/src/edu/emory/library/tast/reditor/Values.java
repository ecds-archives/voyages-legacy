package edu.emory.library.tast.reditor;

import java.util.HashMap;
import java.util.Map;

public class Values
{

	private Map values = new HashMap();
	
	public void removeValueFor(String name)
	{
		values.remove(name);
	}

	public void addValue(FieldValue value)
	{
		values.put(value.getName(), value);
	}

	public FieldValue getValueFor(String name)
	{
		return (FieldValue) values.get(name);
	}

}
