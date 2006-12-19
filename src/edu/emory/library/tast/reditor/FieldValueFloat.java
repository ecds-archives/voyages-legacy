package edu.emory.library.tast.reditor;


public class FieldValueFloat extends FieldValueText
{
	
	public static final String TYPE = "float";

	private boolean parsed = false;
	private boolean valid = false;
	private transient float floatValue;

	public FieldValueFloat(String name)
	{
		super(name);
	}

	public FieldValueFloat(String name, float doubleValue)
	{
		super(name);
		setFloat(doubleValue);
	}
	
	public FieldValueFloat(String name, Float doubleValue)
	{
		super(name);
		setFloat(doubleValue);
	}

	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				floatValue = Float.parseFloat(getValue());
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

	public void setFloat(float floatValue)
	{
		this.floatValue = floatValue;
		super.setValue(String.valueOf(floatValue));
		valid = true;
		parsed = true;
	}

	public void setFloat(Float floatValue)
	{
		if (floatValue != null)
		{
			setFloat(floatValue.floatValue());
		}
		else
		{
			setValue("");
		}
	}
	
	public Float getFloat()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			return new Float(floatValue);
		}
	}

	public float getFloatValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return floatValue;
		}
	}

}