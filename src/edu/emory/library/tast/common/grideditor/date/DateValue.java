package edu.emory.library.tast.common.grideditor.date;

import java.util.Calendar;
import java.util.Date;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.util.StringUtils;

public class DateValue extends Value
{
	
	private String day;
	private String month;
	private String year;

	private boolean parsed = false;
	private boolean valid = false;
	private transient int dayInt;
	private transient int monthInt;
	private transient int yearInt;
	
	public DateValue(String day, String month, String year)
	{
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public DateValue(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
	}

	public DateValue(Calendar cal)
	{
		setDate(cal);
	}
	
	public boolean isEmpty()
	{
		return
			StringUtils.isNullOrEmpty(day) &&
			StringUtils.isNullOrEmpty(month) &&
			StringUtils.isNullOrEmpty(year);
	}
	
	public boolean isValid()
	{
		if (!parsed)
		{
			try
			{
				dayInt = Integer.parseInt(StringUtils.trimAndUnNull(day));
				monthInt = Integer.parseInt(StringUtils.trimAndUnNull(month));
				yearInt = Integer.parseInt(StringUtils.trimAndUnNull(year));
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
	
	public Date getDate()
	{
		if (!isValid())
		{
			return null;
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.set(yearInt, monthInt, dayInt);
			return cal.getTime();
		}
	}
	
	public void setDate(Date date)
	{
		if (date == null)
		{
			setDate((Calendar)null);
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			setDate(cal);
		}
	}
	
	public void setDate(Calendar cal)
	{
		
		if (cal == null)
		{
			
			year = "";
			month = "";
			day = "";
			
			valid = false;
			
		}
		else
		{
		
			yearInt = cal.get(Calendar.YEAR);
			monthInt = cal.get(Calendar.MONTH);
			dayInt = cal.get(Calendar.DAY_OF_MONTH);
			
			year = String.valueOf(yearInt);
			month = String.valueOf(monthInt);
			day = String.valueOf(dayInt);
		
			valid = true;

		}
		
		parsed = true;

	}

	public String getDay()
	{
		return day;
	}

	public String getDayOrEmpty()
	{
		if (day == null)
		{
			return "";
		}
		else
		{
			return day;
		}
	}

	public void setDay(String day)
	{
		parsed = false;
		this.day = day;
	}

	public String getMonth()
	{
		return month;
	}

	public String getMonthOrEmpty()
	{
		if (month == null)
		{
			return "";
		}
		else
		{
			return month;
		}
	}

	public void setMonth(String month)
	{
		parsed = false;
		this.month = month;
	}

	public String getYear()
	{
		return year;
	}

	public String getYearOrEmpty()
	{
		if (year == null)
		{
			return "";
		}
		else
		{
			return year;
		}
	}

	public void setYear(String year)
	{
		parsed = false;
		this.year = year;
	}
	
	public String toString()
	{
		return year + " / " + month + " / " + day;
	}
	
	public boolean equals(Object obj)
	{

		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof DateValue))
			return false;

		DateValue that = (DateValue) obj;
		
		if (this.isValid() && that.isValid())
			return this.getDate().equals(that.getDate());

		return
			StringUtils.compareStrings(this.year, that.year) && 
			StringUtils.compareStrings(this.month, that.month) && 
			StringUtils.compareStrings(this.day, that.day); 
		
	}

}
