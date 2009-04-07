package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.util.StringUtils;

public class TextboxLongValue extends TextboxValue
{
	
	private boolean parsed = false;
	private boolean valid = false;
	private transient long longValue;

	public TextboxLongValue(String text)
	{
		super(text);
	}

	public TextboxLongValue(int value)
	{
		this(Long.toString(value));
		this.longValue = value;
		this.parsed = true;
		this.valid = true;
	}
	
	public TextboxLongValue(Integer value)
	{
		this(value == null ? "": value.toString());
		if (value != null)
		{
			this.longValue = value.intValue();
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
				longValue = Long.parseLong(getText());
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
	
	public long getLongValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return longValue;
		}
	}
	
	public Long getInteger()
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
	
	public void setLongValue(int value)
	{
		super.setText(String.valueOf(value));
		this.longValue = value;
		this.parsed = true;
		this.valid = true;
	}

	public void setInteger(Long value)
	{
		if (value != null)
		{
			this.longValue = value.longValue();
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