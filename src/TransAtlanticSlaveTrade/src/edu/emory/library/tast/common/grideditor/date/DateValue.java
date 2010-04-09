/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
	private Integer[] dt = new Integer[3];

	private boolean parsed = false;
	private boolean valid = false;
	private transient int dayInt;
	private transient int monthInt;
	private transient int yearInt;
	
	public DateValue(String day, String month, String year)
	{
		this.year = year;
		this.month = month;				
		this.day = day;
	}
	
	public DateValue(Date date)
	{
		Calendar cal = Calendar.getInstance();	
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		setDate(cal);
	}

	public DateValue(Calendar cal)
	{
		setDate(cal);
	}
	
	public DateValue(Integer[] dateArr)
	{
		Integer yearI = dateArr[0];
		Integer monthI = dateArr[1];
		Integer dayI = dateArr[2];
		year = "";
		month = "";
		day = "";
		if (yearI != null) {
			year = yearI.toString();
		}
		if (monthI != null) {
			month = monthI.toString();
		}
		if (dayI != null) {
			day = dayI.toString();
		}
		//setDate(cal);
	}
	
	public boolean isEmpty()
	{
		if ("DD".equals(day) && "MM".equals(month) && "YYYY".equals(year)) {
			return true;
		}
		return
			StringUtils.isNullOrEmpty(day) &&
			StringUtils.isNullOrEmpty(month) &&
			StringUtils.isNullOrEmpty(year);
	}
	
	public boolean isValid()
	{
		if (!parsed)
		{
//			if ("DD".equals(day)) day = "" ;
//			if ("MM".equals(month)) month = "" ;
//			if ("YYYY".equals(year)) year = "" ;
			try
			{
				if (!"DD".equals(day)) {
					dayInt = Integer.parseInt(StringUtils.trimAndUnNull(day));
				}
				if (!"MM".equals(month)) {
					monthInt = Integer.parseInt(StringUtils.trimAndUnNull(month));
				}
				if (!"YYYY".equals(year)) {
					yearInt = Integer.parseInt(StringUtils.trimAndUnNull(year));
				}
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
	
	public Integer[] getDateAsInt()
	{
		if (!isValid())
		{
			return null;
		}
		else{
			dt[0] = yearInt;		
			dt[1] = monthInt;
			dt[2] = dayInt;
			return dt;
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
		if (day == null || day.trim().equals("") || day.trim().equals("0"))
		{
			return "DD";
		}
		else
		{
			return day;
		}
	}

	public void setDay(String day)
	{
		if ("DD".equals(day)) {
			day = "";
		}
		parsed = false;
		this.day = day;
	}

	public String getMonth()
	{
		return month;
	}

	public String getMonthOrEmpty()
	{
		if (month == null || month.trim().equals("") || month.trim().equals("0"))
		{
			
			return "MM";
		}
		else
		{
			return month;
		}
	}

	public void setMonth(String month)
	{
		if ("MM".equals(month)) {
			month = "";
		}
		parsed = false;
		this.month = month;
	}

	public String getYear()
	{
		return year;
	}

	public String getYearOrEmpty()
	{
		if (year == null || year.trim().equals("") || year.trim().equals("0"))
		{
			return "YYYY";
		}
		else
		{
			return year;
		}
	}

	public void setYear(String year)
	{
		if ("YYYY".equals(year)) {
			year = "";
		}
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

	public boolean isCorrectValue() {
		if (isEmpty()) {
			return true;
		}
		if (StringUtils.isNullOrEmpty(this.year) &&
				StringUtils.isNullOrEmpty(this.month) && 
				StringUtils.isNullOrEmpty(this.day)) {
			return true;
		}
		return this.isValid();
	}

}
