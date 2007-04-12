package edu.emory.library.tast.common.grideditor.textbox;

public class TextboxIntegerValue extends TextboxValue
{
	
	private boolean parsed = false;
	private boolean valid = false;
	private transient int intValue;

	public TextboxIntegerValue(String text)
	{
		super(text);
	}

	public TextboxIntegerValue(int value)
	{
		this(Integer.toString(value));
		this.intValue = value;
		this.parsed = true;
		this.valid = true;
	}
	
	public TextboxIntegerValue(Integer value)
	{
		this(value == null ? "": value.toString());
		if (value != null)
		{
			this.intValue = value.intValue();
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
				intValue = Integer.parseInt(getText());
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
	
	public int getIntValue()
	{
		if (!isValid())
		{
			return 0;
		}
		else
		{
			return intValue;
		}
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
	
	public void setIntValue(int value)
	{
		super.setText(String.valueOf(value));
		this.intValue = value;
		this.parsed = true;
		this.valid = true;
	}

	public void setInteger(Integer value)
	{
		if (value != null)
		{
			this.intValue = value.intValue();
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