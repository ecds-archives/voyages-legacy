package edu.emory.library.tast.util;

public class ConversionUtils
{
	
	public static Integer toInteger(String str)
	{

		if (str == null)
			return null;
		
		try
		{
			return new Integer(str);
		}
		catch (NumberFormatException nfe)
		{
			return null;
		}
		
	}

}
