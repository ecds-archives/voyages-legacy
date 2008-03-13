package edu.emory.library.tast.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class StringUtils
{
	
	private final static Pattern sepRegex = Pattern.compile("[^a-zA-Z_0-9]+");
	
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
		return isNullOrEmpty(str, false);
	}
	
	public static boolean isNullOrEmpty(String str, boolean trim)
	{
		if (str == null) return true;
		if (trim) str = str.trim();
		return str.length() == 0;
	}

	public static String unNull(String str)
	{
		if (str == null) return "";
		return str;
	}

	public static String coalesce(String str1, String str2)
	{
		if (str1 != null) return str1;
		return str2;
	}

	public static String coalesce(String str1, String str2, String str3)
	{
		if (str1 != null) return str1;
		if (str2 != null) return str2;
		return str3;
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
	
	public static boolean compareStrings(String s1, String s2, boolean nullSameAsEmpty)
	{
		if (s1 == s2)
		{
			return true;
		}
		else if (s1 == null)
		{
			if (s2 == null)
			{
				return true;
			}
			else if (nullSameAsEmpty && s2.length() == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (s2 == null)
		{
			if (s1 == null)
			{
				return true;
			}
			else if (nullSameAsEmpty && s1.length() == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			 return s2.equals(s1);
		}
	}

	public static boolean compareStrings(String s1, String s2)
	{
		return compareStrings(s1, s2, false);
	}

	public static boolean compareStringArrays(String []a1, String []a2, boolean nullSameAsEmpty)
	{
		if (a1 == a2)
		{
			return true;
		}
		else if (a1 == null && a2 == null)
		{
			return true;
		}
		else if (a1 != null && a2 == null)
		{
			 return false;
		}
		else if (a1 == null && a2 != null)
		{
			 return false;
		}
		else if (a1.length != a2.length)
		{
			 return false;
		}
		else
		{
			int n = a1.length;
			for (int i = 0; i < n; i++)
			{
				if (!StringUtils.compareStrings(a1[i], a2[i], nullSameAsEmpty))
				{
					return false;
				}
			}
			return true;
		}
	}
	
	public static boolean compareStringArrays(String []a1, String []a2)
	{
		return compareStringArrays(a1, a2, false);
	}

	public static String join(String sep, Set objects)
	{
		if (objects == null) return "";
		StringBuffer res = new StringBuffer();
		int i = 0;
		for (Iterator iter = objects.iterator(); iter.hasNext();)
		{
			if (i > 0) res.append(sep);
			res.append(iter.next());
			i++;
		}
		return res.toString();
	}

	public static String join(String sep, String[] arr)
	{
		if (arr == null) return "";
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < arr.length; i++)
		{
			if (i > 0) res.append(sep);
			res.append(arr[i]);
		}
		return res.toString();
	}
	
	public static String joinNonEmpty(String sep, String[] arr)
	{
		if (arr == null) return "";
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < arr.length; i++)
		{
			if (!StringUtils.isNullOrEmpty(arr[i]))
			{
				if (i > 0) res.append(sep);
				res.append(arr[i]);
			}
		}
		return res.toString();
	}

	public static Set toStringSet(String[] arr)
	{
		Set set = new HashSet();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				set.add(arr[i]);
			}
		}
		return set;
	}

	public static Set toLongSet(String[] arr)
	{
		Set set = new HashSet();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				set.add(new Long(arr[i]));
			}
		}
		return set;
	}

	public static Set toIntegerSet(String[] arr)
	{
		Set set = new HashSet();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				set.add(new Integer(arr[i]));
			}
		}
		return set;
	}
	
	public static Integer[] parseIntegerArray(String[] arr)
	{
		return parseIntegerArray(arr, false);
	}

	public static Integer[] parseIntegerArray(String[] arr, boolean omitInvalid)
	{
		Integer[] ints = new Integer[arr.length];
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (omitInvalid)
				{
					try
					{
						ints[i] = new Integer(arr[i]);
					}
					catch (NumberFormatException nfe)
					{
					}
				}
				else
				{
					ints[i] = new Integer(arr[i]);
				}
			}
		}
		return ints;
	}

	public static Long[] parseLongArray(String[] arr)
	{
		return parseLongArray(arr, false);
	}

	public static Long[] parseLongArray(String[] arr, boolean omitInvalid)
	{
		Long[] ints = new Long[arr.length];
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (omitInvalid)
				{
					try
					{
						ints[i] = new Long(arr[i]);
					}
					catch (NumberFormatException nfe)
					{
					}
				}
				else
				{
					ints[i] = new Long(arr[i]);
				}
			}
		}
		return ints;
	}

	public static String[] removeEmpty(String[] arr)
	{
		if (arr == null)
		{
			return null;
		}
		else
		{
			List tmp = new ArrayList();
			for (int i = 0; i < arr.length; i++)
			{
				if (!StringUtils.isNullOrEmpty(arr[i]))
				{
					tmp.add(arr[i]);
				}
			}
			String[] newArr = new String[tmp.size()];
			tmp.toArray(newArr);
			return newArr;
		}
	}
	
	public static String[] extractQueryKeywords(String query, boolean upperCase)
	{
		
		if (query == null)
			return new String[] {};
		
		if (upperCase)
			query = query.toUpperCase();
		
		String[] keywords = sepRegex.split(query);
		if (keywords.length == 1 && keywords[0].length() == 0)
		{
			return new String[0];
		}
		else
		{
			return keywords;
		}

	}
	
	public static String[] splitByLines(String str)
	{
		if (str == null)
		{
			return new String[0];
		}
		else
		{
			return str.split("[\n\r]+");
		}
	}
	
	public static String[] splitByLinesAndRemoveEmpty(String str)
	{
		return removeEmpty(splitByLines(str));
	}

	public static String trimEnd(String str, char toTrim)
	{
		if (str == null)
		{
			return null;
		}
		else
		{
			int i = 0;
			for (i = str.length() - 1; 0 <= i; i--)
			{
				if (str.charAt(i) != toTrim)
				{
					break;
				}
			}
			return str.substring(0, i + 1);
		}
	}

}