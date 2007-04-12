package edu.emory.library.tast.common.grideditor.textbox;

public class TextboxFloatValue extends TextboxValue
{
	
	private boolean parsed = false;
	private boolean valid = false;
	private transient float floatValue;

	public TextboxFloatValue(String text)
	{
		super(text);
	}

	public TextboxFloatValue(int value)
	{
		this(Float.toString(value));
		this.floatValue = value;
		this.parsed = true;
		this.valid = true;
	}
	
	public TextboxFloatValue(Float value)
	{
		this(value == null ? "": value.toString());
		if (value != null)
		{
			this.floatValue = value.intValue();
			this.parsed = true;
			this.valid = true;
		}
		else
		{
			this.parsed = true;
			this.valid = false;
		}
	}

	public void setText(String text)
	{
		super.setText(text);
		parsed = false;
	}
	
	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				floatValue = Float.parseFloat(getText());
				valid = true;
			}
			catch(NumberFormatException nfe)
			{
				valid = false;
			} catch (NullPointerException npe) {
				valid = false;
			}
			parsed = true;
		}
		return valid;
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
	
	public void setFloatValue(int value)
	{
		super.setText(String.valueOf(value));
		this.floatValue = value;
		this.parsed = true;
		this.valid = true;
	}

	public void setFloat(Float value)
	{
		if (value != null)
		{
			this.floatValue = value.longValue();
			this.parsed = true;
			this.valid = true;
		}
		else
		{
			this.parsed = true;
			this.valid = false;
		}
	}

}