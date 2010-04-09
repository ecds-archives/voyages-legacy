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
package edu.emory.library.tast.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class StringUtils
{
	
	public static final int CREATE_HASH_SET = 1;
	public static final int CREATE_TREE_SET = 2;
	private static final int DEFAULT_SET_TYPE = 1;
	
	public static final int KEEP_CASE = 1;
	public static final int UPPER_CASE = 2;
	public static final int LOWER_CASE = 3;

	private final static Pattern keywordSepRegex = Pattern.compile("[^a-zA-Z_/0-9]+");
	
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

	public static String join(String sep, Collection objects)
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
	
	private static Set createSet(int setType)
	{
		switch (setType)
		{
			case CREATE_HASH_SET: return new HashSet();
			case CREATE_TREE_SET: return new TreeSet();
			default: return createSet(DEFAULT_SET_TYPE);
		}
	}

	public static Set toStringSet(String[] arr, int setType)
	{
		Set set = createSet(setType);
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				set.add(arr[i]);
			}
		}
		return set;
	}

	public static Set toStringSet(String[] arr)
	{
		return toStringSet(arr, DEFAULT_SET_TYPE);
	}

	public static Set toLongSet(String[] arr, boolean omitInvalid, int setType)
	{
		Set set = new HashSet();
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (omitInvalid)
				{
					try
					{
						set.add(new Long(arr[i]));
					}
					catch (NumberFormatException nfe)
					{
					}
				}
				else
				{
					set.add(new Long(arr[i]));
				}
			}
		}
		return set;
	}
	
	public static Set toLongSet(String[] arr, boolean omitInvalid)
	{
		return toLongSet(arr, omitInvalid, DEFAULT_SET_TYPE);
	}
	
	public static Set toLongSet(String[] arr)
	{
		return toLongSet(arr, false, DEFAULT_SET_TYPE);
	}

	public static Set toIntegerSet(String[] arr, int hashSet)
	{
		Set set = createSet(hashSet);
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				set.add(new Integer(arr[i]));
			}
		}
		return set;
	}
	
	public static Set toIntegerSet(String[] arr)
	{
		return toIntegerSet(arr, DEFAULT_SET_TYPE);
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
	
	public static String[] extractQueryKeywords(String query, int changeCase)
	{
		
		if (query == null)
			return new String[] {};
		
		if (changeCase == UPPER_CASE)
			query = query.toUpperCase();
		else if (changeCase == LOWER_CASE)
			query = query.toLowerCase();
		
		String[] keywords = keywordSepRegex.split(query);
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
	
	public static char getFirstLetter(String str)
	{
		if (str == null) return '-';
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (Character.isLetter(c)) return c;
		}
		return ' ';
	}

	public static String getFirstElement(String[] arr)
	{
		if (arr == null || arr.length == 0)
			return null;
		else
			return arr[0];
	}
	
	public static String[] getAllSubstrings(String str)
	{
		if (str == null)
			return new String[0];
		
		int n = str.length();
		String[] substrings = new String[n * (n + 1) / 2];
		
		int k = 0;
		for (int i = 0; i < n; i++)
		{
			for (int j = i + 1; j < n+1; j++)
			{
				substrings[k++] = str.substring(i, j);
			}
		}
		
		return substrings;
		
	}
	
	public static String replaceChars(String str, char[] oldChars, char[] newChars)
	{
		if (str == null) return null;
		boolean changed = false;
		char[] strChars = str.toCharArray();
		for (int i = 0; i < strChars.length; i++)
		{
			for (int j = 0; j < oldChars.length; j++)
			{
				if (strChars[i] == oldChars[j])
				{
					strChars[i] = newChars[j];
					changed = true;
					break;
				}
			}
		}
		if (changed)
		{
			return new String(strChars);
		}
		else
		{
			return str;
		}
	}
	
	public static Long[] toLongArray(Set stringSet)
	{
		Long[] longs = new Long[stringSet.size()];
		int i = 0;
		for (Iterator iterator = stringSet.iterator(); iterator.hasNext();)
		{
			String valueStr = (String) iterator.next();
			try
			{
				longs[i++] = new Long(valueStr);
			}
			catch (NumberFormatException nfe)
			{
				return null;
			}
		}
		return longs;
	}

}