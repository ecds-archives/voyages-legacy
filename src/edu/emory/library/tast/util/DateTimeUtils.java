package edu.emory.library.tast.util;

import java.util.Date;

public class DateTimeUtils
{
	
	private static final long[] timeUnitLengths = new long[] {
		7*24*60*60*1000,
		24*60*60*1000,
		60*60*1000,
		60*1000,
		1000,
		1 };
	
	private static final String[] timeUnitNamesPlular = new String[] {
		"weeks",
		"days",
		"hours",
		"min",
		"sec",
		"ms" };

	private static final String[] timeUnitNamesSingular = new String[] {
		"week",
		"day",
		"hour",
		"min",
		"sec",
		"ms" };
	
	public static final int TIME_INTERVAL_NO_ROUNDING = -1; 
	public static final int TIME_INTERVAL_ROUND_TO_MS = 5; 
	public static final int TIME_INTERVAL_ROUND_TO_SEC = 4; 
	public static final int TIME_INTERVAL_ROUND_TO_MIN = 3; 
	public static final int TIME_INTERVAL_ROUND_TO_HOURS = 2; 
	public static final int TIME_INTERVAL_ROUND_TO_DAYS = 1; 
	public static final int TIME_INTERVAL_ROUND_TO_WEEKS = 0; 

	public static String formatTimeSpan(Date start, Date end)
	{
		return formatTimeSpan(end.getTime() - start.getTime());
	}

	public static String formatTimeSpan(Date start, Date end, int roundTo)
	{
		return formatTimeSpan(end.getTime() - start.getTime(), roundTo);
	}
	
	public static String formatTimeSpan(long length)
	{
		return formatTimeSpan(length, TIME_INTERVAL_NO_ROUNDING);
	}

	public static String formatTimeSpan(long length, int roundTo)
	{

		StringBuffer text = new StringBuffer();
		
		if (0 <= roundTo && roundTo < timeUnitLengths.length)
		{
			long smallestUnit = timeUnitLengths[roundTo];
			long smallestReminder = length % smallestUnit; 
			if (2 * smallestReminder >= smallestUnit)
			{
				length = length - smallestReminder + smallestUnit;   
			}
			else
			{
				length = length - smallestReminder;   
			}
		}
		
		for (int i = 0; i < timeUnitLengths.length; i++)
		{
			long unitLength = timeUnitLengths[i];
			long noOfUnits = length / unitLength;
			length = length % unitLength;
			if (noOfUnits > 0)
			{
				if (text.length() > 0) text.append(" ");
				text.append(noOfUnits);
				text.append(" ");
				if (noOfUnits == 1)
					text.append(timeUnitNamesSingular[i]);
				else if (noOfUnits > 1)
					text.append(timeUnitNamesPlular[i]);
			}
			if (i == roundTo)
			{
				if (text.length() == 0)
				{
					text.append("0 ");
					text.append(timeUnitNamesPlular[i]);
				}
				break;
			}
		}
		
		return text.toString();

	}
	
	private static void test(long length, int roundTo)
	{
		System.out.println(length + " = " + DateTimeUtils.formatTimeSpan(length, roundTo));
	}
	
	public static void main(String[] args)
	{
//		test(1000, TIME_INTERVAL_NO_ROUNDING);
//		test(1054, TIME_INTERVAL_NO_ROUNDING);
//		test(2000, TIME_INTERVAL_NO_ROUNDING);
//		test(2011, TIME_INTERVAL_ROUND_TO_MS);
		test(2011, TIME_INTERVAL_ROUND_TO_SEC);
		test(500, TIME_INTERVAL_ROUND_TO_SEC);
		test(499, TIME_INTERVAL_ROUND_TO_SEC);
		test(499, TIME_INTERVAL_ROUND_TO_WEEKS);
	}

}
