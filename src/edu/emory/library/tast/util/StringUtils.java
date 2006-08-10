package edu.emory.library.tast.util;

public class StringUtils
{
	
	public static int indexOf(String key, String[] haystack)
	{
		if (haystack == null)
			return -1;
		
		for (int i=0; i<haystack.length; i++)
		{
			if (haystack[i].compareTo(key) == 0)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public static String trimAndUnNull(String str)
	{
		if (str == null) return "";
		return str.trim();
	}

	public static String trimAndUnNull(String str, int maxLength)
	{
		if (str == null) return "";
		str = str.trim();
		if (str.length() > maxLength) str = str.substring(0, maxLength);
		return str;
	}
	
	public static boolean isNullOrEmpty(String str)
	{
		return str == null || str.length() == 0;
	}
	
	public static String getProjectionStringForProj4(String[] array) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			String string = array[i];
			buffer.append("+").append(string).append(" ");
		}
		return buffer.toString();
	}
	
	public static String getProjectionStringForMapFile(String[] array) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			String string = array[i];
			buffer.append("\"").append(string).append("\"\n");
		}
		return buffer.toString();
	}
	
	public static boolean compareStrings(String s1, String s2)
	{
		if (s1 == null && s2 == null)
		{
			return true;
		}
		else if (s1 != null)
		{
			 return s1.equals(s2);
		}
		else
		{
			 return s2.equals(s1);
		}
	}

}
