package edu.emory.library.tast.reditor;


public class FieldValueInteger extends FieldValueText
{
	
	public static final String TYPE = "integer";

	private boolean parsed = false;
	private boolean valid = false;
	private transient int intValue;

	public FieldValueInteger(String name)
	{
		super(name);
	}

	public FieldValueInteger(String name, int intValue)
	{
		super(name);
		setInteger(intValue);
	}
	
	public FieldValueInteger(String name, Integer intValue)
	{
		super(name);
		setInteger(intValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				intValue = Integer.parseInt(getValue());
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
	
	public Integer getInteger()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Integer(intValue);
		}
	}
	
	public void setInteger(int intValue)
	{
		this.intValue = intValue;
		super.setValue(String.valueOf(intValue));
		valid = true;
		parsed = true;
	}

	public void setInteger(Integer intValue)
	{
		if (intValue != null)
		{
			setInteger(intValue.intValue());
		}
		else
		{
			setValue("");
		}
	}
	

}