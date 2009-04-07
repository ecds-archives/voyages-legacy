package edu.emory.library.tast.util;

public class EqualsUtil
{

	static public boolean areEqual(boolean a, boolean b)
	{
		return a == b;
	}

	static public boolean areEqual(char a, char b)
	{
		return a == b;
	}

	static public boolean areEqual(byte a, byte b)
	{
		return a == b;
	}

	static public boolean areEqual(int a, int b)
	{
		return a == b;
	}
	
	static public boolean areEqual(long a, long b)
	{
		return a == b;
	}

	static public boolean areEqual(float a, float b)
	{
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}

	static public boolean areEqual(double a, double b)
	{
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}

	static public boolean areEqual(Object a, Object b)
	{
		return a == null ? b == null : a.equals(b);
	}

	static public boolean areEqual(String[] a, String[] b)
	{
		
		if (a == null && b == null)
			return true;

		if (a == null || b == null)
			return false;
		
		if (a.length != b.length)
			return false;
		
		for (int i = 0; i < a.length; i++)
			if (!EqualsUtil.areEqual(a[i], b[i]))
				return false;
		
		return true;

	}

}