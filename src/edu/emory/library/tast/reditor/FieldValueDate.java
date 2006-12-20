package edu.emory.library.tast.reditor;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.StringUtils;

public class FieldValueDate extends FieldValue
{
	
	public static final String TYPE = "date";

	private String day;
	private String month;
	private String year;

	private boolean parsed = false;
	private boolean valid = false;
	private transient int dayInt;
	private transient int monthInt;
	private transient int yearInt;

	public FieldValueDate(String name)
	{
		super(name);
	}
	
	public FieldValueDate(String name, Date date)
	{
		super(name);
		setDate(date);
	}

	public static String getHtmlDayFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name + "_" + "day";
	}

	public static String getHtmlMonthFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name + "_" + "month";
	}

	public static String getHtmlYearFieldName(UIComponent component, FacesContext context, String name)
	{
		return component.getClientId(context) + "_" + name + "_" + "year";
	}

	public void decode(UIComponent component, FacesContext context)
	{
		
		String dayFieldName = FieldValueDate.getHtmlDayFieldName(component, context, getName());
		String monthFieldName = FieldValueDate.getHtmlDayFieldName(component, context, getName());
		String yearFieldName = FieldValueDate.getHtmlDayFieldName(component, context, getName());
		
		Map params = context.getExternalContext().getRequestParameterMap();
		day = (String) params.get(dayFieldName);
		month = (String) params.get(monthFieldName);
		year = (String) params.get(yearFieldName);
		
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
			
			year = "";
			month = "";
			day = "";
			
			valid = false;
			
		}
		else
		{
		
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
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

}