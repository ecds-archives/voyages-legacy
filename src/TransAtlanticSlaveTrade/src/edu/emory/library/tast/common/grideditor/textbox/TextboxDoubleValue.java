package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.util.StringUtils;

public class TextboxDoubleValue extends TextboxValue
{
	
	private boolean parsed = false;
	private boolean valid = false;
	private transient double doubleValue;

	public TextboxDoubleValue(String text)
	{
		super(text);
	}

	public TextboxDoubleValue(double value)
	{
		this(Double.toString(value));
		this.doubleValue = value;
		this.parsed = true;
		this.valid = true;
	}
	
	public TextboxDoubleValue(Double value)
	{
		this(value == null ? "": value.toString());
		if (value != null)
		{
			this.doubleValue = value.intValue();
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
				doubleValue = Double.parseDouble(getText());
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
	
	public Double getFloat()
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
	
	public void setDoubleValue(double value)
	{
		super.setText(String.valueOf(value));
		this.doubleValue = value;
		this.parsed = true;
		this.valid = true;
	}

	public void setFloat(Double value)
	{
		if (value != null)
		{
			this.doubleValue = value.longValue();
			this.parsed = true;
			this.valid = true;
		}
		else
		{
			this.parsed = true;
			this.valid = false;
		}
	}

	public boolean isCorrectValue() {
		if (StringUtils.isNullOrEmpty(this.getText())) {
			return true;
		}
		return isValid();
	}
}