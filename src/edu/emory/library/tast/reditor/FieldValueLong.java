package edu.emory.library.tast.reditor;


public class FieldValueLong extends FieldValueText
{
	
	public static final String TYPE = "long";

	private boolean parsed = false;
	private boolean valid = false;
	private transient long longValue;

	public FieldValueLong(String name)
	{
		super(name);
	}

	public FieldValueLong(String name, long longValue)
	{
		super(name);
		setLong(longValue);
	}
	
	public FieldValueLong(String name, Long longValue)
	{
		super(name);
		setLong(longValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				longValue = Long.parseLong(getValue());
				valid = true;
			}
			catch(NumberFormatException nfe)
			{
				valid = false;
			}
			parsed = true;
		}
		return valid;
	}
	
	public Long getLong()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Long(longValue);
		}
	}
	
	public void setLong(long longValue)
	{
		this.longValue = longValue;
		super.setValue(String.valueOf(longValue));
		valid = true;
		parsed = true;
	}

	public void setLong(Long longValue)
	{
		if (longValue != null)
		{
			setLong(longValue.longValue());
		}
		else
		{
			setValue("");
		}
	}

}