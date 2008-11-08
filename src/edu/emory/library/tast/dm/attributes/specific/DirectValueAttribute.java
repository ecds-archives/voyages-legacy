package edu.emory.library.tast.dm.attributes.specific;

import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;

public class DirectValueAttribute extends Attribute
{

	Object value;

	public DirectValueAttribute(Object value)
	{
		super(null, null);
		this.value = value;
	}

	public DirectValueAttribute(String name, Object value)
	{
		super(name, null);
		this.value = value;
	}

	public String getTypeDisplayName()
	{
		return null;
	}

	public boolean isOuterjoinable()
	{
		return false;
	}

	public String getHQLSelectPath(Map bindings)
	{
		StringBuffer buffer = new StringBuffer();
		if (this.value instanceof String)
		{
			buffer.append("'").append(value).append("'");
		}
		else
		{
			buffer.append(value);
		}
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings)
	{
		return this.getHQLSelectPath(bindings);
	}

	public String getHQLOuterJoinPath(Map bindings)
	{
		return null;
	}

	public String getSQLReference(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		StringBuffer buffer = new StringBuffer();
		if (this.value instanceof String)
		{
			buffer.append("'").append(value).append("'");
		}
		else
		{
			buffer.append(value);
		}
		return buffer.toString();
	}
}
