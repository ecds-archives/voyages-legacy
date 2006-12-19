package edu.emory.library.tast.reditor;


public class FieldValueDouble extends FieldValueText
{
	
	public static final String TYPE = "double";

	private boolean parsed = false;
	private boolean valid = false;
	private transient double doubleValue;

	public FieldValueDouble(String name)
	{
		super(name);
	}
	
	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				doubleValue = Double.parseDouble(getValue());
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
	
	public void setValue(String value)
	{
		parsed = false;
		super.setValue(value);
	}

	public void setDouble(double doubleValue)
	{
		this.doubleValue = doubleValue;
		super.setValue(String.valueOf(doubleValue));
		valid = true;
		parsed = true;
	}

	public void setDouble(Double doubleValue)
	{
		if (doubleValue != null)
		{
			setDouble(doubleValue.doubleValue());
		}
		else
		{
			setValue("");
		}
	}
	
	public Double getDouble()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Double(doubleValue);
		}
	}

	public double getDoubleValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return doubleValue;
		}
	}

}